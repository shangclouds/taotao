package com.taotao.test;

import com.sun.javadoc.SeeTag;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class TestJedis {
    //测试单机版jedis
//   @Test
    public void jedis(){
        //创建jedis对象，需要ip及端口号
        Jedis jedis=new Jedis("192.168.25.133",6379);
        //直接操作数据库
        jedis.set("redis-key","123");
        String result = jedis.get("redis-key");
        System.out.println(result);
        //关闭jedis对象
        jedis.close();
    }
    //测试连接池
//    @Test
    public void TestJedisPool(){
        //创建连接池对象
        JedisPool jedisPool=new JedisPool("192.168.25.133",6379);
        //从连接池中获取连接
        Jedis jedis=jedisPool.getResource();
        //操作数据库
        String s = jedis.get("redis-key");
        System.out.println(s);
        //关闭连接，方便连接池回收(在方法内执行S)
        jedis.close();
        //在关闭系统前关闭连接池
        jedisPool.close();
    }
    //测试集群
//    @Test
    public void TestCluster(){
       //创建JedisCluster对象，构造参数Set类型，集合中每个元素为HostAndPort类型
        Set<HostAndPort> nodes=new HashSet<>();
        nodes.add(new HostAndPort("192.168.25.128",7001));
        nodes.add(new HostAndPort("192.168.25.128",7002));
        nodes.add(new HostAndPort("192.168.25.128",7003));
        nodes.add(new HostAndPort("192.168.25.128",7004));
        nodes.add(new HostAndPort("192.168.25.128",7005));
        nodes.add(new HostAndPort("192.168.25.128",7006));
        JedisCluster jedisCluster=new JedisCluster(nodes);
        //利用JedisCluster对象操作redis，JedisCluster自带连接池，可以是单例的
//        jedisCluster.set("redis-key","123");
        String result = jedisCluster.get("key1");
        System.out.println(result);
        //在系统关闭之前关闭JedisCluster
//        jedisCluster.close();
    }
}
