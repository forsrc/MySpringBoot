package com.forsrc.boot.jms.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

//@Component
public class UserQueue {

    public static final String QUEUE_NAME = "q/user";
    @Resource(name = "queueJmsTemplate")
    private JmsTemplate jmsTemplate;
    @Resource(name = "userQueueBean")
    private Queue userQueueBean;
    @Resource(name = "queueConnectionFactory")
    private ConnectionFactory queueConnectionFactory;

    @Bean(name = "userQueueBean")
    public Queue userQueueBean() {
        return new ActiveMQQueue(QUEUE_NAME);
    }

    public void send(final UserMessage userMessage) {
        MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    String json = mapper.writeValueAsString(userMessage);
                    return session.createTextMessage(json);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return session.createTextMessage(e.getMessage());
                }

            }
        };
        jmsTemplate.send(userQueueBean, messageCreator);
    }

//    @Bean(name = "queueDefaultMessageListenerContainer")
//    public DefaultMessageListenerContainer queueDefaultMessageListenerContainer() {
//        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
//        container.setPubSubDomain(false);
//        container.setConnectionFactory(queueConnectionFactory);
//        container.setupMessageListener(new UserQueueReceiver());
//        container.setDestination(userQueueBean);
//        return container;
//    }
    @Bean(name = "userQueueDefaultMessageListenerContainer")
    public DefaultMessageListenerContainer UserQueueDefaultMessageListenerContainer(
            @Autowired
            @Qualifier("queueConnectionFactory") ConnectionFactory connectionFactory,
            @Autowired
            @Qualifier("userQueueReceiver") MessageListener messageListener,
            @Autowired
            @Qualifier("userQueueBean") Destination destination) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setPubSubDomain(false);
        container.setConnectionFactory(connectionFactory);
        container.setupMessageListener(messageListener);
        container.setDestination(destination);
        return container;
    }

    @Component(value = "userQueueReceiver")
    public class UserQueueReceiver implements MessageListener {

        @JmsListener(destination = QUEUE_NAME, containerFactory = "queueJmsListenerContainerFactory")
        public void onMessage(Message message) {
            System.out.println(String.format("--> onMessage() UserQueue: %s", message));
            try {
                if (message instanceof ActiveMQTextMessage) {
                    String json = ((ActiveMQTextMessage) message).getText();
                    if (json != null && json.startsWith("{") && json.endsWith("}")) {
                        ObjectMapper mapper = new ObjectMapper();
                        UserMessage userMessage = mapper.readValue(json, UserMessage.class);
                        System.out.println(String.format("--> UserQueue: %s", userMessage.toString()));
                        return;
                    }
                    System.out.println(String.format("--> UserQueue: %s", json));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
