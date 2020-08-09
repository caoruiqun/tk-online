package com.taikang.auth.client;

import com.taikang.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @description: 用户feignclient
 * @author: CaoRuiqun
 * @create: 2020-06-20 12:40
 **/
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
