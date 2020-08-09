package com.taikang.search.client;

import com.taikang.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author CaoRuiqun on 2020/4/22
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
