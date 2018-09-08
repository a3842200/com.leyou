package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 45207
 * @create 2018-08-25 16:10
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi{

}
