package com.taikang.search.repository;

import com.taikang.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author CaoRuiqun on 2020/4/22
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
