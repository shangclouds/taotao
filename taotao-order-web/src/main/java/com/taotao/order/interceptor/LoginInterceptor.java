package com.taotao.order.interceptor;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program:taotao
 * @description:登录拦截器
 * @author:xjw
 * @create:2018-03-04 13:36
 **/
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${TT_TOKEN}")
    private String TT_TOKEN;
    @Value("${SS0_URL}")
    private String SS0_URL;
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      //执行handle之前执行此方法，一个请求对应一个handler，handler是方法，controller是类
        //返回true，放行，返回false，拦截
        //业务逻辑
        //1.从请求中获取token
        String token = CookieUtils.getCookieValue(request, TT_TOKEN);
        //2.如果未取到token，则返回到登录页面，并且需要将当前请求的url作为参数传过去，登录成功后跳转回来
        if (StringUtils.isBlank(token)){
            //取当前请求的url
            String url = request.getRequestURL().toString();
            //跳转到登录页面，页面跳转有两个方式，forward和redirect
            response.sendRedirect(SS0_URL+"page/login?url"+url);

            //拦截
            return false;
        }
        //3.如果取到token，到sso中调用服务
        TaotaoResult result = userService.getUserByToken(token);
        //4.但是取不到用户信息，说明登录信息过期，返回登录页面，并且登录后需要跳转回来
        if (result.getStatus()!=20){
            //取当前请求的url
            String url = request.getRequestURL().toString();
            //跳转到登录页面，页面跳转有两个方式，forward和redirect
            response.sendRedirect(SS0_URL+"page/login?url"+url);

            //拦截
            return false;
        }
        //5.获取用户信息
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user",user);
        //6.如果获取到用户信息，放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        //执行handler之后，返回modelAndview之前，此方法对midelAndView进行处理
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        //在modelAndView返回之后,通常是进行异常处理
    }
}
