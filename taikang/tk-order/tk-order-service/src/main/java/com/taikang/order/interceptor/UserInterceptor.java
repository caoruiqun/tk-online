package com.taikang.order.interceptor;

import com.taikang.auth.entity.UserInfo;
import com.taikang.auth.utils.JwtUtils;
import com.taikang.common.utils.CookieUtils;
import com.taikang.order.config.JwtProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 拦截器
 * @author: CaoRuiqun
 * @create: 2020-06-20 21:34
 **/
public class UserInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UserInterceptor.class);

    private JwtProperties jwtProperties;

    public UserInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 定义一个线程域，存放登录用户
     */
    private static final ThreadLocal<UserInfo> t1 = new ThreadLocal<>();

    /**
     *      * 在业务处理器处理请求之前被调用
     *      * 如果返回false
     *      *      则从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     *      * 如果返回true
     *      *      执行下一个拦截器，直到所有拦截器都执行完毕
     *      *      再执行被拦截的Controller
     *      *      然后进入拦截器链
     *      *      从最后一个拦截器往回执行所有的postHandle()
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.查询token
        String token = CookieUtils.getCookieValue(request,jwtProperties.getCookieName());
        if (StringUtils.isBlank(token)){
            //2.未登录，返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        //3.有token，查询用户信息
        try{
            //4.解析成功，说明已经登录
            UserInfo userInfo = JwtUtils.getInfoFromToken(token,jwtProperties.getPublicKey());
            //5.放入线程域
            t1.set(userInfo);
            return true;
        }catch (Exception e){
            //6.抛出异常，证明未登录，返回401
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            log.error("[购物车微服务]解析用户身份失败.", e);
            return false;
        }
    }

    /**
     * 在业务处理器处理请求执行完成后，生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        super.postHandle(request, response, handler, modelAndView);
//    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //防止内存泄漏
        t1.remove();
    }

    public static UserInfo getLoginUser(){
        return t1.get();
    }


}
