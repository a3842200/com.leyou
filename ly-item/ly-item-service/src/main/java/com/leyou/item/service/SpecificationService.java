package com.leyou.item.service;

import com.leyou.common.enumeration.ExceptionEnumeration;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 45207
 * @create 2018-08-20 22:45
 */
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> querySpecGroupsByCid(Long cid) {
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(group);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnumeration.GOODS_GROUP_SPECIFICATION_ERROR);
        }
        return list;
    }

    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setGeneric(generic);
        param.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(param);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnumeration.GOODS_PARAM_SPECIFICATION_ERROR);
        }
        return list;

    }


    /*public void saveSpecParams(SpecParam specParam) {
        int count = specParamMapper.insert(specParam);
        if (count == 0) {
            throw new LyException(ExceptionEnumeration.SPECPARAM_ADD_ERROR);
        }
    }*/


    public List<SpecGroup> querySpecGroupsIncludingSpecParamsByCid(Long cid) {
        //先查组
        List<SpecGroup> specGroups = querySpecGroupsByCid(cid);
        //查询当前分类下的所有参数
        List<SpecParam> params = querySpecParams(null, cid, null, null);
        //把param放到一个Map中,key是组id,值是组内所有参数
        Map<Long, List<SpecParam>> map = new HashMap<>();
        for (SpecParam param : params) {
            //判断当前参数所属的组在map中是否存在
            if (!map.containsKey(param.getGroupId())) {
                map.put(param.getGroupId(), new ArrayList<>());
            }
            //存到param集合中
            map.get(param.getGroupId()).add(param);
        }
        //循环存储param数据
        for (SpecGroup specGroup : specGroups) {
            specGroup.setSpecParams(map.get(specGroup.getId()));
        }
        return specGroups;
    }
}
