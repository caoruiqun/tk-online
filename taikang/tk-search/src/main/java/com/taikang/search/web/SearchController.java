package com.taikang.search.web;

import com.taikang.common.vo.PageResult;
import com.taikang.search.pojo.Goods;
import com.taikang.search.pojo.SearchRequest;
import com.taikang.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-25 16:04
 **/
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
    * 搜索功能
    * @Param [searchRequest]
    * @return
    */
    @PostMapping("/page")
    public ResponseEntity<PageResult<Goods>> search(@RequestBody SearchRequest searchRequest){
        PageResult<Goods> result = searchService.search(searchRequest);
        return ResponseEntity.ok(result);
    }

}