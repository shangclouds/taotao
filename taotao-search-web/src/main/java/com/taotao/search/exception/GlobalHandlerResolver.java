package com.taotao.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalHandlerResolver implements HandlerExceptionResolver{
    private static final Logger logger= LoggerFactory.getLogger(GlobalHandlerResolver.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        //写入日志
        logger.debug("测试handler的类型"+handler.getClass());
        logger.info("进入全局异常处理器");
        logger.error("系统发送异常",e);
        //发送邮件
        //利用jmail客户端发送
        //发送短信
        //跳转到错误界面
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("message","网络异常，请稍后重试");
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
