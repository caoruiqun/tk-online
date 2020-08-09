package com.taikang.search.service.impl;

import com.taikang.common.vo.PageResult;
import com.taikang.item.pojo.Brand;
import com.taikang.item.pojo.Category;
import com.taikang.item.pojo.SpecParam;
import com.taikang.item.pojo.Spu;
import com.taikang.search.client.BrandClient;
import com.taikang.search.client.CategoryClient;
import com.taikang.search.client.GoodsClient;
import com.taikang.search.client.SpecificationClient;
import com.taikang.search.pojo.Goods;
import com.taikang.search.pojo.SearchRequest;
import com.taikang.search.pojo.SearchResult;
import com.taikang.search.repository.GoodsRepository;
import com.taikang.search.service.SearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-25 16:14
 **/
public class SearchServiceImpl implements SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Override
    public PageResult<Goods> search(SearchRequest searchRequest) {
        int page = searchRequest.getPage() - 1;
        int size = searchRequest.getSize();
        String key = searchRequest.getKey();

        //创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        //分页
        queryBuilder.withPageable(PageRequest.of(page, size));
        //搜索条件
        //过滤
//        QueryBuilder basicQuery = QueryBuilders.matchQuery("all", key);
        QueryBuilder basicQuery = buildBasicQuery(searchRequest);
        queryBuilder.withQuery(basicQuery);
        //聚合分类和品牌
        //聚合分类
        String categoryAggName = "category";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        //品牌进行聚合
        String brandAggName = "brand";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        //查询
//        Page<Goods> result = goodsRepository.search(queryBuilder.build());
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        //解析结果
        //解析分页结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Goods> goodsList = result.getContent();
//        PageResult<Goods> pageResult = new PageResult<>(total, (long) totalPage, goodsList);
        //解析聚合结果
        Aggregations aggs = result.getAggregations();
        //1 商品分类的聚合结果
        List<Category> categories = parseCategoryAgg(aggs.get(categoryAggName));
        //2 品牌的聚合结果
        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));
        //3 处理规格参数聚合
        List<Map<String, Object>> specs = null;
        if (categories != null && categories.size() == 1) {
            //如果商品分类只有一个进行聚合，并根据分类与基本查询条件聚合
            specs = buildSpecificationAgg(categories.get(0).getId(), basicQuery);
        }

        return new SearchResult(total, (long) totalPage, goodsList, categories, brands, specs);
    }


    private QueryBuilder buildBasicQuery(SearchRequest searchRequest) {
        //创建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()));
        //过滤条件
        Map<String, String> map = searchRequest.getFilter();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            //处理key
            if (!"cid3".equals(key) && !"brandId".equals(key)) {
                key = "specs." + key + ".keyword";
            }
            String value = entry.getValue();
            queryBuilder.filter();
        }
        return null;
    }

    private List<Map<String, Object>> buildSpecificationAgg(Long cid, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        //查询需要聚合的规格参数
        List<SpecParam> specParamList = specificationClient.getSpecParam(null, cid, true);
        //聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //1.带上条件
        queryBuilder.withQuery(basicQuery);
        //2.聚合
        for (SpecParam specParam : specParamList) {
            String name = specParam.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        }
        //获取结果
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        //解析结果
        Aggregations aggs = result.getAggregations();
        for (SpecParam specParam : specParamList) {
            //规格参数名
            String name = specParam.getName();
            StringTerms terms = aggs.get(name);
            List<String> options = terms.getBuckets().stream().map(b -> b.getKeyAsString()).collect(Collectors.toList());
            //准备map
            Map<String, Object> map = new HashMap<>();
            map.put("k", name);
            map.put("options", options);
            specs.add(map);
        }
        return specs;
    }

    private List<Brand> parseBrandAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Brand> brandList = brandClient.queryBrandByIds(ids);
            return brandList;
        } catch (Exception e) {
            return null;
        }
    }

    private List<Category> parseCategoryAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue()).collect(Collectors.toList());
            List<Category> categoryList = categoryClient.queryCategoryByIds(ids);
            return categoryList;
        } catch (Exception e) {
            log.error("[搜索服务]查询分类异常：", e);
            return null;
        }
    }



    @Override
    public void createOrUpdateIndex(Long spuId) {
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //构建goods
//        Goods goods = buildGoods(spu);
        //存入索引库
//        goodsRepository.save(goods);

    }

    @Override
    public void deleteIndex(Long spuId) {
        goodsRepository.deleteById(spuId);
    }

}
