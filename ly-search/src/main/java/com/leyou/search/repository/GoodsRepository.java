package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 45207
 * @create 2018-08-25 17:22
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long>{

}
