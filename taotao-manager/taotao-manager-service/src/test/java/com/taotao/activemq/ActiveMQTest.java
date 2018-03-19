package com.taotao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

/**
 * @program:taotao
 * @description:测试activemq
 * @author:xjw
 * @create:2018-02-23 21:35
 **/
public class ActiveMQTest {

//    @Test
    public void testProducer()throws Exception{
        //1.创建一个连接工程对象ConnectionFactory,参数为activemq服务的ip和端口号
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        //2.使用连接工程对象创建连接connection
        Connection connection = connectionFactory.createConnection();
        //3.开启连接，调用connection的start方法
        connection.start();
        //4.使用connection对象创建一个session对象
        //第一个参数为是否开启事务，一般为不开启(分布式事务)，保证最后结果一致即可
        // 如果第一个参数为true,则第二个参数自动忽略，第二个参数为应答模式：手动响应还是自动响应，一般为自动响应
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、使用Session对象创建一个Destination对象，两种形式Queue和topic
        Queue queue = session.createQueue("test-queue");
        //6.使用Session对象创建一个producer对象
        MessageProducer producer = session.createProducer(queue);
        //7.创建TextMessage
//        TextMessage textMessage=new ActiveMQTextMessage();
//        textMessage.setText("hello activemq");
        TextMessage textMessage = session.createTextMessage("hello activeMQ 23");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();

    }
//    @Test
    public void testConsumer()throws Exception{
        //1.创建连接工厂对象connectionFactory，参数为activemq服务的ip和端口号
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        //2.创建连接对象connection
        Connection connection = connectionFactory.createConnection();
        //3.启动连接
        connection.start();
        //4.通过connection创建Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.利用Session创建Destination
        Queue queue = session.createQueue("test-queue");
        //6.利用session创建consumer
        MessageConsumer consumer = session.createConsumer(queue);
        //7.向consumer对象中设置MessageListener对象，用来接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //取消息的内容
                if (message instanceof TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //使方法一直等待
        System.in.read();
        //关闭连接
        consumer.close();
        session.close();
        connection.close();
    }
    //activemq topic producer
    public void testTopicProducer() throws Exception{
        //1.创建连接工厂类，需要制定activemq服务的ip及端口号
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        //2.利用连接工厂类生产连接
        Connection connection = connectionFactory.createConnection();
        //3.开启连接
        connection.start();
        //4.利用连接创建会话Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.利用session对象创建Destination,采用topic模式
        Topic topic = session.createTopic("test-topic");
        //5.利用session对象创建producer对象
        MessageProducer producer = session.createProducer(topic);
        //6.利用session创建TextMessage，制定消息发送内容
        TextMessage textMessage = session.createTextMessage("hello，topic");
        //7.发送消息
        producer.send(textMessage);
        //8.关闭连接

        producer.close();
        session.close();
        connection.close();

    }
    //activemq consumer topic
    public void testTopicConsumer() throws Exception{
        //1.创建连接工厂
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        //2.创建连接诶
        Connection connection = connectionFactory.createConnection();
        //3.启动连接
        connection.start();
        //4、利用connection对象创建连接对象
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //5.利用Session创建Destination
        Topic topic = session.createTopic("test-topic");
        //6.利用session创建consumer
        MessageConsumer consumer = session.createConsumer(topic);
        //7.向consumer对象中设置MessageListener对象，用来接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //取消息的内容
                if (message instanceof TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //使方法一直等待
        System.in.read();
        //关闭连接
        consumer.close();
        session.close();
        connection.close();

    }
}
