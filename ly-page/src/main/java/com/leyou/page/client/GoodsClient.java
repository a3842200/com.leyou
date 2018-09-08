package com.leyou.page.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 45207
 * @create 2018-08-25 16:34
 */

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi{

}
