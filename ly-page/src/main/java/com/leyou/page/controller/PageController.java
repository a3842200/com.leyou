package com.leyou.page.controller;

import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author 45207
 * @create 2018-09-01 17:36
 */

@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @RequestMapping("/item/{spuId}.html")
    public String toGoodsPage(@PathVariable("spuId") Long spuId, Model model){
        //准备模型数据
        Map<String, Object> data = pageService.loadModel(spuId);
        model.addAllAttributes(data);

        //创建html(交给线程处理,线程抛了异常不会影响主线程)
        pageService.synCreateHtml(spuId);

        //返回视图名
        return "item";
    }
}
