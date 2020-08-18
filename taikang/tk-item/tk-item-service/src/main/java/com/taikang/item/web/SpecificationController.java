package com.taikang.item.web;

import com.taikang.item.pojo.SpecGroup;
import com.taikang.item.pojo.SpecParam;
import com.taikang.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-13 00:36
 **/
@RestController
@RequestMapping("/spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /** 
    * 根据分类id查询规格组
    * @Param [cid] 
    * @return 
    */
//    http://api.leyou.com/api/item/spec/groups/76
    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupsByCid(@PathVariable("cid") Long cid) {
        List<SpecGroup> specGroupList = specificationService.querySpecGroupsByCid(cid);
        return ResponseEntity.ok(specGroupList);
    }

    /** 
    * 根据组id查询规格参数
    * @Param [gid] 
    * @return 
    */
////    http://api.leyou.com/api/item/spec/params?gid=1
//    @GetMapping("/params")
//    public ResponseEntity<List<SpecParam>> querySpecParamInfo(@RequestParam("gid") Long gid) {
//        List<SpecParam> specParamList = specificationService.querySpecParamInfo(gid);
//        return ResponseEntity.ok(specParamList);
//    }


    /**
    * 根据条件查询规格参数
    * @Param [gid, cid, searching]
    * @return
    */
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParamInfo(@RequestParam(value = "gid",required = false) Long gid,
                                                              @RequestParam(value = "cid",required = false) Long cid,
                                                              @RequestParam(value = "searching",required = false) Boolean searching) {
        List<SpecParam> specParamList = specificationService.querySpecParamInfo(gid,cid,searching);
        return ResponseEntity.ok(specParamList);
    }


    /**
    * 根据分类查询组和组内参数
    * @Param [cid]
    * @return
    */
    @GetMapping("/group")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@RequestParam("cid") Long cid) {
        List<SpecGroup> specGroupList = specificationService.querySpecsByCid(cid);
        return ResponseEntity.ok(specGroupList);
    }

}
