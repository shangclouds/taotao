package com.taotao.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * @program:taotao
 * @description:利用jmstemplate发送消息
 * @author:xjw
 * @create:2018-02-24 11:43
 **/
public class SpringActiveMQ {

    //利用jmstemplate发送消息
    @Test
    public  void TestTemplateSendMessage(){
        //初始化spring 容器
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring/*.xml");
        //获取jmsTemplate
        JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsTemplate");
        //获取Destination
        Destination destination = (Destination) context.getBean("test-queue");
        //发送消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("spring activemq send  queue message");
                return textMessage;
            }
        });
    }
}
