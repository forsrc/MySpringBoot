package com.forsrc.boot.jms.amq;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqTest {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private TopicUser userActiveMQTopic;

    @Test
    public void test() throws InterruptedException {
        System.out.println(jmsTemplate);
        ActiveMQQueue queue = new ActiveMQQueue("q/user");
        MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(new Date().toString());
            }
        };
        jmsTemplate.send(queue, messageCreator);

        userActiveMQTopic.send("OK : " + new Date().toString());
        System.out.println("----------");
        TimeUnit.SECONDS.sleep(1);
    }
}
