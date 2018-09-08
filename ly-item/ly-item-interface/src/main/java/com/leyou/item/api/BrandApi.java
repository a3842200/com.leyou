package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 45207
 * @create 2018-08-25 16:52
 */
@RequestMapping("brand")
public interface BrandApi {
    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    Brand queryBrandById(@PathVariable("id")Long id);

    @GetMapping("list")
    List<Brand> queryBrandsByIds(@RequestParam("ids") List<Long> ids);
}
