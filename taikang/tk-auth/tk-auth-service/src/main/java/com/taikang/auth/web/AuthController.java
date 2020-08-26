package com.taikang.auth.web;

import com.taikang.TkAuthApplication;
import com.taikang.auth.config.JwtProperties;
import com.taikang.auth.entity.UserInfo;
import com.taikang.auth.service.AuthService;
import com.taikang.auth.utils.JwtUtils;
import com.taikang.common.enums.ExceptionEnum;
import com.taikang.common.exception.TkException;
import com.taikang.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-06-20 11:32
 **/
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录授权
     *
     * @return
     * @Param [username, password]
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        //登录
        String token = authService.login(username, password);

        //写入cookie
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, true);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 校验用户登录状态
     *
     * @return
     * @Param
     */
    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("TK_TOKEN") String token,
                                           HttpServletRequest request, HttpServletResponse response) {
        try {
            //解析Token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            //2.解析成功要重新刷新token
            token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            //3.更新Cookie中的token
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, true);
            //已登录，返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            //Token已过期，或者被篡改
            throw new TkException(ExceptionEnum.UNAUTHORIZED);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
