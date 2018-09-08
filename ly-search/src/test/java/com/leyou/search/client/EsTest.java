package com.leyou.search.client;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 45207
 * @create 2018-08-25 17:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTest {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsRepository repository;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void testCreate(){
        //创建索引库
        template.createIndex(Goods.class);
        template.getMapping(Goods.class);
    }

    @Test
    public void testLoadData(){
        //准备一堆的spu
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            PageResult<Spu> result = goodsClient.querySpuByPage(page, rows, true, null);
            List<Spu> spus = result.getItems();

            if (CollectionUtils.isEmpty(spus)) {
                break;
            }

            List<Goods> goodsList = spus.stream().map(spu -> searchService.buildGoods(spu)).collect(Collectors.toList());

            repository.saveAll(goodsList);

            size = spus.size();
            page++;
        } while (size == 100);
    }
}
