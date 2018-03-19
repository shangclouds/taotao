package com.taotao.test;

import com.taotao.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisClientTest {
    @Test
    public void TestJedisPool(){
        //启动spring容器
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        JedisClient jedisClient = context.getBean(JedisClient.class);//获取接口的实现类对象
        //利用jedis客户端操作redis
        jedisClient.set("jedis-pool","测试单机版");
        System.out.println(jedisClient.get("jedis-pool"));
    }
}
