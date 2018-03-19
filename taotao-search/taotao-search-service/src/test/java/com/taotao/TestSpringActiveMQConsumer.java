package com.taotao;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @program:taotao
 * @description:测试接收消息
 * @author:xjw
 * @create:2018-02-24 15:07
 **/
public class TestSpringActiveMQConsumer {
    @Test
    public void testSpringActiveMQConsumer(){
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring/*.xml");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
