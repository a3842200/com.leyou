package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 45207
 * @create 2018-08-25 16:49
 */
@RequestMapping("category")
public interface CategoryApi {
    //ResponseEnity<>只是给springMVC看的,所以这边可以省略(不省略也是可以的),直接List<>...
    @GetMapping("list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids")List<Long> ids);
}
