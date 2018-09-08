package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 45207
 * @create 2018-08-20 22:43
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据商品分类id查询规格参数组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupsByCid(@PathVariable("cid")Long cid){
        List<SpecGroup> list = specificationService.querySpecGroupsByCid(cid);
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据组id查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public  ResponseEntity<List<SpecParam>> querySpecParams(
            //组id
            @RequestParam(value = "gid", required = false)Long gid,
            //分类id
            @RequestParam(value = "cid", required = false)Long cid,
            //是否通用
            @RequestParam(value = "generic", required = false)Boolean generic,
            //是否可以搜索
            @RequestParam(value = "searching", required = false)Boolean searching
            ){
        return ResponseEntity.ok(specificationService.querySpecParams(gid, cid, generic, searching));
    }

    /*@PostMapping("params")
    public ResponseEntity<Void> saveSpecParams(@RequestBody SpecParam specParam){
        specificationService.saveSpecParams(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }*/

    /**
     * 根据商品分类id查询所有规格参数组和规格参数
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupsIncludingSpecParamsByCid(@PathVariable("cid")Long cid) {
        List<SpecGroup> list = specificationService.querySpecGroupsIncludingSpecParamsByCid(cid);
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

}
