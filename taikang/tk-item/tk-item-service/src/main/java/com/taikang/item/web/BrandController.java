package com.taikang.item.web;

import com.taikang.item.pojo.Brand;
import com.taikang.common.vo.PageResult;
import com.taikang.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-03-14 14:53
 **/
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
    * 分页查询品牌
    * @Param [pageNum, pageSize, orderBy, desc, key]
    * @return
    */
    @GetMapping("/page")
//    public ResponseEntity<Page<Brand>> getBrandListByPage(@RequestParam(value = "page",defaultValue = "1") Integer pageNum,
    public ResponseEntity<PageResult<Brand>> getBrandListByPage(@RequestParam(value = "page",defaultValue = "1") Integer pageNum,
                                                                @RequestParam(value = "rows",defaultValue = "5") Integer pageSize,
                                                                @RequestParam(value = "sortBy",required = false) String orderBy,
                                                                @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
                                                                @RequestParam(value = "key",required = false) String key) {
//        Page<Brand> brandPage = brandService.getBrandListByPage(pageNum, pageSize, orderBy, desc, key);
        PageResult<Brand> result = brandService.getBrandListByPage(pageNum, pageSize, orderBy, desc, key);

        return ResponseEntity.ok(result);
    }

    /**
    * 新增品牌
    * @Param [brand, cid]
    * @return
    */
    @PostMapping("/add")
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cid") List<Long> cidList) {
        brandService.addBrand(brand, cidList);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /** 
    * 根据category的id查询品牌信息
    * @Param [cid] 
    * @return 
    */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> getBrandbyCid(@PathVariable("cid") Long cid) {
        List<Brand> brandList = brandService.getBrandbyCid(cid);
        return ResponseEntity.ok(brandList);
    }


    /**
     * 根据品牌id查询品牌信息
     * @param id
     * @return
     */
    @GetMapping("/list/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        Brand brand = brandService.queryBrandById(id);
        return ResponseEntity.ok(brand);
    }


    @GetMapping("/brands")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        return  ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }

}
