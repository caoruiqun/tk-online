package com.taikang.search.service;

import com.taikang.common.vo.PageResult;
import com.taikang.search.pojo.Goods;
import com.taikang.search.pojo.SearchRequest;

/**
 * @author CaoRuiqun on 2020/4/25
 */
public interface SearchService {
    /**
    * 商品搜索
    * @Param [searchRequest]
    * @return
    */
    PageResult<Goods> search(SearchRequest searchRequest);

    void createOrUpdateIndex(Long spuId);

    void deleteIndex(Long spuId);
}
