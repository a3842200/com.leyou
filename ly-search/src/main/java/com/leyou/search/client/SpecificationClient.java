package com.leyou.search.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author 45207
 * @create 2018-08-25 16:54
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
