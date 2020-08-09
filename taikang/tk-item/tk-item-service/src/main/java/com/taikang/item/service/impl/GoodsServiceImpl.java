package com.taikang.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taikang.common.enums.ExceptionEnum;
import com.taikang.common.exception.TkException;
import com.taikang.common.vo.PageResult;
import com.taikang.item.mapper.*;
import com.taikang.item.pojo.*;
import com.taikang.item.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-13 22:44
 **/
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public PageResult<Spu> getSpuByPage(Integer pageNum, Integer pageSize, Boolean saleable, String key) {


        //过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        //搜索字段过滤
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }

        //是否上架过滤
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }

        //默认排序
        example.setOrderByClause("last_update_time DESC");

        //分页
        PageHelper.startPage(pageNum, pageSize);

        //查询
        List<Spu> spuList = spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spuList)) {
            throw new TkException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        //解析分类及品牌名称
        loadCategoryAndBrandName(spuList);

        //解析分页结果
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spuList);
        return new PageResult<>(spuPageInfo.getTotal(),spuList);
    }

    /**
    *
    * @Param [spu]
    * @return
    */
    @Override
    @Transactional
    public void addGoods(Spu spu) {
        //新增spu
//        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);

        int count = spuMapper.insert(spu);
        if (count != 1) {
            throw new TkException(ExceptionEnum.GOOD_SAVE_ERROR);
        }

        //新增spuDetail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());

        count = spuDetailMapper.insert(spuDetail);
        if (count != 1) {
            throw new TkException(ExceptionEnum.GOOD_SAVE_ERROR);
        }

        //新增SKU和库存
        addSkuAndStock(spu);

        //发送mq消息
        amqpTemplate.convertAndSend("item.insert", spu.getId());
    }

    /**
    * 新增SKU和库存
    * @Param [spu]
    * @return
    */
    private void addSkuAndStock(Spu spu) {
        int count;
        //定义库存集合
        List<Stock> stockList = new ArrayList<>();

        //新增sku
        List<Sku> skuList = spu.getSkus();
        for (Sku sku : skuList) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());

            count = skuMapper.insert(sku);
            if (count != 1) {
                throw new TkException(ExceptionEnum.GOOD_SAVE_ERROR);
            }

            //新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);
//            count = stockMapper.insert(stock);
//            if (count != 1) {
//                throw new TkException(ExceptionEnum.GOOD_SAVE_ERROR);
//            }
            //新增的逻辑放在for循环里不太好，用批处理比较好
        }

        //批量新增库存
        count = stockMapper.insertList(stockList);
        if (count != stockList.size()) {
            throw new TkException(ExceptionEnum.GOOD_SAVE_ERROR);
        }
    }

    @Override
    public SpuDetail getSpuDetailById(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if (null == spuDetail) {
            throw new TkException(ExceptionEnum.GOOD_DETAIL_ERROR);
        }
        return spuDetail;
    }

    @Override
    public List<Sku> getSkuListBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new TkException(ExceptionEnum.GOOD_SKU_ERROR);
        }
//        for (Sku s : skuList) {
//            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
//            if (null == stock) {
//                throw new TkException(ExceptionEnum.GOOD_STOCK_ERROR);
//            }
//            s.setStock(stock.getStock());
//        }
        //查询库存
        List<Long> idList = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        loadStockInSku(idList, skuList);
        return skuList;
    }

    @Override
    @Transactional
    public void updateGoods(Spu spu) {
        if (spu.getId() == null) {
            throw new TkException(ExceptionEnum.GOOD_ID_ERROR);
        }
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(skuList)) {
            //删除SKU
            int count = skuMapper.delete(sku);
            if (count != skuList.size()) {
                throw new TkException(ExceptionEnum.GOOD_UPDATE_ERROR);
            }
            //删除库存
            List<Long> idList = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            count = stockMapper.deleteByIdList(idList);
            if (count != idList.size()) {
                throw new TkException(ExceptionEnum.GOOD_UPDATE_ERROR);
            }
        }

        //修改SPU
        spu.setLastUpdateTime(new Date());
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if (count != 1) {
            throw new TkException(ExceptionEnum.GOOD_UPDATE_ERROR);
        }

        //修改detail
        SpuDetail spuDetail = spu.getSpuDetail();
        count = spuDetailMapper.updateByPrimaryKeySelective(spuDetail);
        if (count != 1) {
            throw new TkException(ExceptionEnum.GOOD_UPDATE_ERROR);
        }

        //新增SKU和库存
        addSkuAndStock(spu);

        //发送mq消息
        amqpTemplate.convertAndSend("item.update", spu.getId());


       /* //新增SKU
        List<Sku> skus = spu.getSkus();
        for (Sku s : skuList) {
            s.setCreateTime(new Date());
            s.setLastUpdateTime(sku.getCreateTime());
            s.setSpuId(spu.getId());
        }
        count = skuMapper.insertList(skus);
        if (count != skus.size()) {
            throw new TkException(ExceptionEnum.GOOD_UPDATE_ERROR);
        }

        Sku selectSku = new Sku();
        selectSku.setSpuId(spu.getId());
        skuList = skuMapper.select(selectSku);

        //新增库存
        List<Stock> stockList = new ArrayList<>();
        for (Sku s : skuList) {
            Stock stock = new Stock();
            stock.setSkuId(s.getId());
            stock.setStock(s.getStock());
            stockList.add(stock);
        }
        count = stockMapper.insertList(stockList);
        if (count != skus.size()) {
            throw new TkException(ExceptionEnum.GOOD_UPDATE_ERROR);
        }*/
    }

    @Override
    public Spu querySpuById(Long id) {
        //查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (null == spu) {
            throw new TkException(ExceptionEnum.GOODS_NOT_FOUND);
        }

        //查询sku
        List<Sku> skuList = getSkuListBySpuId(id);
        spu.setSkus(skuList);

        //查询detail
        SpuDetail spuDetail = getSpuDetailById(id);
        spu.setSpuDetail(spuDetail);

        return spu;
    }

    @Override
    public List<Sku> getSkuListByIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skus)) {
            throw new TkException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        //查询库存
        loadStockInSku(ids, skus);
        return skus;
    }


    private void loadStockInSku(List<Long> ids, List<Sku> skus) {
        //查询库存
        List<Stock> stockList = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stockList)) {
            throw new TkException(ExceptionEnum.GOOD_STOCK_ERROR);
        }
        //把stock变成map，key：skuId，value：库存
        Map<Long, Long> stockMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skus.forEach(s -> s.setStock(stockMap.get(s.getId())));
    }

    /**
    * 解析品牌与分类名
    * @Param [spuList]
    * @return
    */
    private void loadCategoryAndBrandName(List<Spu> spuList) {
        for (Spu spu : spuList) {
            //处理分类名称
            List<Long> idList = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
            List<Category> categoryList = categoryMapper.selectByIdList(idList);
            if (CollectionUtils.isEmpty(categoryList)) {
                throw new TkException(ExceptionEnum.CATEGORY_NOT_FOUND);
            }
            List<String> nameList = categoryList.stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(nameList,"/"));

            //处理品牌名称
            Long brandId = spu.getBrandId();
            Brand brand = brandMapper.selectByPrimaryKey(brandId);
            if (null == brand) {
                throw new TkException(ExceptionEnum.BRAND_NOT_FOUND);
            }
            spu.setBname(brand.getName());
        }
    }


    /**
     * 根据skuId查询sku
     * @param id
     * @return
     */
    @Override
    public Sku querySkuById(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }
}
