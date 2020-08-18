package com.taikang.item.api;

import com.taikang.common.vo.PageResult;
import com.taikang.item.pojo.Sku;
import com.taikang.item.pojo.Spu;
import com.taikang.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author CaoRuiqun on 2020/4/22
 */
public interface GoodsApi {

    /**
     * 分页查询SPU
     * @return
     * @Param [pageNum, pageSize, saleable, key]
     */
    @GetMapping("/spu/page")
    PageResult<Spu> querySpuByPage(@RequestParam(value = "page", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "rows", defaultValue = "5") Integer pageSize,
                                   @RequestParam(value = "saleable", required = false) Boolean saleable,
                                   @RequestParam(value = "key", required = false) String key);


    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);


    /**
     * 根据Spu的id查询商品详情spuDetail
     * @return
     * @Param [spuId]
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable("id") Long spuId);


    /**
     * 根据spuId查询spu下面所有的sku
     * @return
     * @Param [spuId]
     */
    @GetMapping("/sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long spuId);




    /**
     * 根据sku的id查询sku
     * @param id
     * @return
     */
    @GetMapping("/sku/{id}")
    Sku querySkuById(@PathVariable("id") Long id);

    /**
     * 根据sku的id批量查询所有sku
     * @Param [spuId]
     * @return
     */
    @GetMapping("/sku/list/ids")
    List<Sku> querySkuBySpuId(@RequestParam("ids") List<Long> ids);

}


