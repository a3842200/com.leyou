package com.leyou.page.service;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.Spu;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 45207
 * @create 2018-09-01 22:58
 */
@Service
@Slf4j
public class PageService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;

    //线程池
    @Autowired
    private static final ExecutorService es = Executors.newFixedThreadPool(20);

    @Value("${ly.page.filePath}")
    private String destPath;

    public Map<String,Object> loadModel(Long spuId) {
        Map<String, Object> map = new HashMap<>();
        try {
            //1.查询spu
            Spu spu = goodsClient.querySpuById(spuId);
            map.put("spu", spu);
            //2.查询skus
            map.put("skus",spu.getSkus());
            //3.查询spuDetail
            map.put("detail", spu.getSpuDetail());
            //4.查询三个级的分类
            List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            map.put("categories", categories);
            //5.查询品牌
            Brand brand = brandClient.queryBrandById(spu.getBrandId());
            map.put("brand", brand);
            //6.查询规格参数
            List<SpecGroup> SpecGroupsIncludingSpecParams = specificationClient.querySpecGroupsIncludingSpecParamsByCid(spu.getCid3());
            map.put("specs", SpecGroupsIncludingSpecParams);
            return map;

        } catch (Exception e) {
            throw new RuntimeException("加载数据失败!", e);
        }
    }

    //传一个spuId
    public void createHtml(Long spuId){

        //准备文件关联的输出流(获取目标文件路径)
        File dest = getFilePath(spuId);

        //如果文件已经存在,备份,存到另外一个地方去
        File bak = new File(spuId + "_bak.html");

        //放在try里面会自动关闭,不需要我们再关流了(这里service层try了,其他层调用就不用再try了)
        try (PrintWriter writer = new PrintWriter(dest, "UTF-8");){
            //准备数据
            Map<String, Object> model = loadModel(spuId);

            //准备context
            Context context = new Context();
            context.setVariables(model);

            //判断目标文件是否存在
            if (dest.exists()) {
                //重命名,剪切
                dest.renameTo(bak);
            }

            //生成文件
            templateEngine.process("item", context, writer);

        } catch (Exception e) {
            //将备份文件进行还原
            dest.renameTo(bak);
            //{}是占位符
            log.error("页面创建失败,{}",e.getMessage(), e);
            throw new RuntimeException("创建页面失败");
        } finally {
            //删除备份文件
            bak.deleteOnExit();
        }
    }

    private File getFilePath(Long spuId) {
        //目标目录
        File dir = new File(destPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //返回文件地址
        return new File(dir, spuId + ".html");
    }

    public void synCreateHtml(Long spuId){
        es.submit(() -> createHtml(spuId));
        /*es.submit(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*/
    }

    public void deleteHtml(Long spuId) {
        File file = getFilePath(spuId);
        if (file.exists()) {
            file.deleteOnExit();
        }
    }
}
