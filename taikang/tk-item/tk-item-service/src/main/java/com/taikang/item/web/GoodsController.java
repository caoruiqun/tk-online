package com.taikang.item.web;

import com.taikang.common.vo.PageResult;
import com.taikang.item.pojo.Sku;
import com.taikang.item.pojo.Spu;
import com.taikang.item.pojo.SpuDetail;
import com.taikang.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-13 22:46
 **/
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
    * 分页查询SPU
    * @Param [pageNum, pageSize, saleable, key]
    * @return
    */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> getSpuByPage(@RequestParam(value = "page",defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "rows",defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "saleable",required = false) Boolean saleable,
                                                        @RequestParam(value = "key",required = false) String key) {
        PageResult<Spu> result = goodsService.getSpuByPage(pageNum, pageSize, saleable, key);
        return ResponseEntity.ok(result);
    }


    /**
    * 商品新增
    * @Param [spu]
    * @return
    */
    @PostMapping("/goods")
    public ResponseEntity<Void> addGoods(@RequestBody Spu spu) {
        goodsService.addGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 商品修改
     * @Param [spu]
     * @return
     */
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu) {
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
    * 根据Spu的id查询商品详情
    * @Param [spuId]
    * @return
    */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> getSpuDetailById(@PathVariable("id") Long spuId) {
        SpuDetail spuDetail = goodsService.getSpuDetailById(spuId);
        return ResponseEntity.ok(spuDetail);
    }


    /**
    * 根据spuId查询sku
    * @Param [spuId]
    * @return
    */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> getSkuListBySpuId(@RequestParam("id") Long spuId) {
        List<Sku> skuList = goodsService.getSkuListBySpuId(spuId);
        return ResponseEntity.ok(skuList);
    }


    /**
     * 根据sku的id查询所有sku
     * @Param [spuId]
     * @return
     */
    @GetMapping("/sku/list/ids")
    public ResponseEntity<List<Sku>> getSkuListBySpuId(@RequestParam("ids") List<Long> ids) {
        List<Sku> skuList = goodsService.getSkuListByIds(ids);
        return ResponseEntity.ok(skuList);
    }


    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = this.goodsService.querySpuById(id);
        return ResponseEntity.ok(spu);
    }

    /**
     * 根据id查询sku
     * @param id
     * @return
     */
    @GetMapping("/sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id") Long id){
        Sku sku = this.goodsService.querySkuById(id);
        if (sku == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(sku);
    }

}
