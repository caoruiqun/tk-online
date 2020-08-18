package com.taikang.item.service.impl;

import com.taikang.common.enums.ExceptionEnum;
import com.taikang.common.exception.TkException;
import com.taikang.item.mapper.SpecGroupMapper;
import com.taikang.item.mapper.SpecParamMapper;
import com.taikang.item.pojo.SpecGroup;
import com.taikang.item.pojo.SpecParam;
import com.taikang.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-04-13 00:42
 **/
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
    * 根据分类id查询规格组
    * @Param [cid]
    * @return
    */
    @Override
    public List<SpecGroup> querySpecGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(list)) {
            throw new TkException(ExceptionEnum.GROUP_NOT_FOUND);
        }
        return list;
    }

    /**
    * 根据条件查询规格参数
    * @Param [gid]
    * @return
    */
    @Override
    public List<SpecParam> querySpecParamInfo(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(list)) {
            throw new TkException(ExceptionEnum.PARAM_NOT_FOUND);
        }
        return list;
    }

    @Override
    public List<SpecGroup> querySpecsByCid(Long cid) {
        //查询规格组
        List<SpecGroup> specGroupList = querySpecGroupsByCid(cid);
        //查询当前分类下的参数
        List<SpecParam> specParamList = querySpecParamInfo(null, cid, null);
        //先把规格参数变为map，map的key是规格组id，map的值是组下的所有参数
        Map<Long, List<SpecParam>> map = new HashMap<>();
        for (SpecParam param : specParamList) {
            if (!map.containsKey(param.getGroupId())) {
                //这个组id在map中不存在，新增一个list
                map.put(param.getGroupId(), new ArrayList<>());
            }
            map.get(param.getGroupId()).add(param);
        }

        //填充param到group
        for (SpecGroup specGroup : specGroupList) {
            specGroup.setParams(map.get(specGroup.getId()));
        }

        return specGroupList;
    }

}
