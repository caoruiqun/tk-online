package com.taikang.gateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.taikang.auth.entity.UserInfo;
import com.taikang.auth.utils.JwtUtils;
import com.taikang.common.utils.CookieUtils;
import com.taikang.gateway.config.FilterProperties;
import com.taikang.gateway.config.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 登录拦截器
 * @author: CaoRuiqun
 * @create: 2020-06-20 14:54
 **/
@Component
@EnableConfigurationProperties({JwtProperties.class,FilterProperties.class}) //实体类不加@component这里就要加这个注解
public class AuthFilter extends ZuulFilter {

    @Autowired
    private JwtProperties properties;

    @Autowired
    private FilterProperties filterProperties;

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);


    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE; //过滤器类型，前置过滤
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1; //过滤顺序
    }

    //是否过滤
    @Override
    public boolean shouldFilter() {
        //1.获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //2.获取request
        HttpServletRequest request = context.getRequest();
        //3.获取路径
        String requestUri = request.getRequestURI();
        logger.info(requestUri);
        //4.判断白名单 判断是否放行。放行，则返回false
        return !isAllowPath(requestUri);
    }

    private boolean isAllowPath(String requestUri) {
        //1.定义一个标记
        boolean flag = false;

        //2.遍历允许访问的路径
//        List<String> paths = Arrays.asList(this.filterProperties.getAllowPaths().split(" "));
        for (String path : filterProperties.getAllowPaths()){
            if (requestUri.startsWith(path)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public Object run() throws ZuulException {
        //1.获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //2.获取request
        HttpServletRequest request = context.getRequest();
        //3.获取cookie中的token
        String token = CookieUtils.getCookieValue(request, properties.getCookieName());
        //4.解析Token
        try {
            //4.1 校验通过，放行
            UserInfo user = JwtUtils.getInfoFromToken(token, this .properties.getPublicKey());
            //TODO 校验权限
        } catch (Exception e) {
            //4.2 校验不通过，返回403
            //解析Token失败，未登录，拦截
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        }
        return null;
    }

}

