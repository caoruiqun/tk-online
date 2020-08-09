package com.taikang.item.api;

import com.taikang.item.pojo.SpecGroup;
import com.taikang.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author CaoRuiqun on 2020/4/22
 */
public interface SpecificationApi {

    /**
     * 根据分类id查询分组
     *
     * @return
     * @Param [cid]
     */
    @GetMapping("/spec/groups/{cid}")
    List<SpecGroup> getSpecGroupsByCid(@PathVariable("cid") Long cid);


    /**
     * 根据条件查询规格参数
     *
     * @return
     * @Param [gid, cid, searching]
     */
    @GetMapping("/spec/params")
    List<SpecParam> getSpecParam(@RequestParam(value = "gid", required = false) Long gid,
                                 @RequestParam(value = "cid", required = false) Long cid,
                                 @RequestParam(value = "searching", required = false) Boolean searching);


    @GetMapping("/spec/group")
    List<SpecGroup> queryGroupByCid(@RequestParam("cid") Long cid);

}
