package com.forsrc.boot.jms.amq;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqTest {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Test
    public void test() {
        System.out.println(jmsMessagingTemplate);
    }
}
