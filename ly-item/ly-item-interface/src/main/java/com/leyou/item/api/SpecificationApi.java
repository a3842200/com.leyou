package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 45207
 * @create 2018-08-25 16:53
 */
@RequestMapping("spec")
public interface SpecificationApi {
    /**
     * 根据组id查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    List<SpecParam> querySpecParams(
            //组id
            @RequestParam(value = "gid", required = false)Long gid,
            //分类id
            @RequestParam(value = "cid", required = false)Long cid,
            //是否通用
            @RequestParam(value = "generic", required = false)Boolean generic,
            //是否可以搜索
            @RequestParam(value = "searching", required = false)Boolean searching
    );

    /**
     * 根据商品分类id查询所有规格参数组和规格参数
     *
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    List<SpecGroup> querySpecGroupsIncludingSpecParamsByCid(@PathVariable("cid") Long cid);
}
