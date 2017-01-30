package com.forsrc.boot.jms.amq;

import com.forsrc.boot.jms.user.UserMessage;
import com.forsrc.boot.jms.user.UserQueue;
import com.forsrc.boot.jms.user.UserTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqTest {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private UserTopic userTopic;
    @Autowired
    private UserQueue userQueue;
    @Test
    public void test() throws InterruptedException {
        System.out.println(jmsTemplate);
        jmsTemplate.send("q/user", new MessageCreator() {
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
