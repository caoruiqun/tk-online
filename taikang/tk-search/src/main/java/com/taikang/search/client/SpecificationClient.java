package com.taikang.search.client;

import com.taikang.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author CaoRuiqun on 2020/4/22
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
