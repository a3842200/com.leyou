package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 45207
 * @create 2018-08-25 16:52
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
