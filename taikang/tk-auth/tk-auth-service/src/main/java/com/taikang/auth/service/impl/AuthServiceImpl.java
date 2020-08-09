package com.taikang.auth.service.impl;

import com.taikang.auth.client.UserClient;
import com.taikang.auth.config.JwtProperties;
import com.taikang.auth.entity.UserInfo;
import com.taikang.auth.service.AuthService;
import com.taikang.auth.utils.JwtUtils;
import com.taikang.common.enums.ExceptionEnum;
import com.taikang.common.exception.TkException;
import com.taikang.user.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-06-20 12:16
 **/
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public String login(String username, String password) {
        try {
            //校验用户名和密码
            User user = userClient.queryUser(username, password);
            if (null == user) {
                throw new TkException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
            }

            //生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), jwtProperties.getPrivateKey(), jwtProperties.getExpire());

            return token;
        } catch (Exception e) {
            log.error("[授权中心]用户名或密码错误，用户名称:{}", username,e);
            throw new TkException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
    }

}
