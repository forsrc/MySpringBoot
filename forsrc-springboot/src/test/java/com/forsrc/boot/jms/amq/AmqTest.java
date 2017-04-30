package com.forsrc.boot.jms.amq;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.junit4.SpringRunner;

import com.forsrc.boot.jms.user.UserMessage;
import com.forsrc.boot.jms.user.UserQueue;
import com.forsrc.boot.jms.user.UserTopic;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqTest {

    @Resource(name = "topicJmsTemplate")
    private JmsTemplate topicJmsTemplate;
    @Resource(name = "queueJmsTemplate")
    private JmsTemplate queueJmsTemplate;
    @Autowired
    private UserTopic userTopic;
    @Autowired
    private UserQueue userQueue;

    @Test
    public void test() throws InterruptedException {
        System.out.println(String.format("--> queueJmsTemplate: %s", queueJmsTemplate));
        System.out.println(String.format("--> topicJmsTemplate: %s", topicJmsTemplate));
        queueJmsTemplate.send("q/user", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(new Date().toString());
            }
        });

        topicJmsTemplate.send("t/user", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(new Date().toString());
            }
        });

        userQueue.send(new UserMessage(new Date().toString()));
        userTopic.send(new UserMessage(new Date().toString()));
        System.out.println("----------");
        TimeUnit.SECONDS.sleep(3);
    }
}
