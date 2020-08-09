package com.taikang.item.service;

import com.taikang.item.pojo.SpecGroup;
import com.taikang.item.pojo.SpecParam;

import java.util.List;

/**
 * @author CaoRuiqun on 2020/4/13
 */
public interface SpecificationService {
    List<SpecGroup> getSpecGroupsByCid(Long cid);

    List<SpecParam> getSpecParam(Long gid, Long cid, Boolean searching);

    List<SpecGroup> querySpecsByCid(Long cid);
}
