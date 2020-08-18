package com.taikang.item.service;

import com.taikang.common.vo.PageResult;
import com.taikang.item.pojo.Sku;
import com.taikang.item.pojo.Spu;
import com.taikang.item.pojo.SpuDetail;

import java.util.List;

/**
 * @author CaoRuiqun on 2020/4/13
 */
public interface GoodsService {
    PageResult<Spu> querySpuByPage(Integer pageNum, Integer pageSize, Boolean saleable, String key);

    void addGoods(Spu spu);

    SpuDetail querySpuDetailById(Long spuId);

    List<Sku> querySkuBySpuId(Long spuId);

    void modifyGoods(Spu spu);

    Spu querySpuById(Long id);

    List<Sku> getSkuListByIds(List<Long> ids);

    Sku querySkuById(Long id);
}
