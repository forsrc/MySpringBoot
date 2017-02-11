package com.forsrc.boot.jms.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

//@Component
public class UserTopic {

    public static final String TOPIC_NAME = "t/user";
    public static final String SUBSCRIBER_NAME = "s/user";
    @Resource(name = "topicJmsTemplate")
    private JmsTemplate jmsTemplate;
    @Resource(name = "userTopicBean")
    private Topic userTopicBean;
    @Resource(name = "topicJmsListenerContainerFactory")
    private JmsListenerContainerFactory topicJmsListenerContainerFactory;
    @Resource(name = "topicConnectionFactory")
    private ConnectionFactory topicConnectionFactory;

    @Bean(name = "userTopicBean")
    public Topic userTopicBean() {
        return new ActiveMQTopic(TOPIC_NAME);
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
        jmsTemplate.send(userTopicBean, messageCreator);
    }

    //    @Bean(name = "topicDefaultMessageListenerContainer")
//    public DefaultMessageListenerContainer topicDefaultMessageListenerContainer() {
//        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
//        container.setPubSubDomain(true);
//        container.setConnectionFactory(topicConnectionFactory);
//        container.setupMessageListener(new UserTopicReceiver());
//        container.setDestination(userTopicBean);
//        return container;
//    }
    @Bean(name = "userTopicDefaultMessageListenerContainer")
    public DefaultMessageListenerContainer UserTopicDefaultMessageListenerContainer(
            @Autowired
            @Qualifier("topicConnectionFactory") ConnectionFactory connectionFactory,
            @Autowired
            @Qualifier("userTopicReceiver") MessageListener messageListener,
            @Autowired
            @Qualifier("userTopicBean") Destination destination) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setPubSubDomain(true);
        container.setConnectionFactory(connectionFactory);
        container.setupMessageListener(messageListener);
        container.setDestination(destination);
        return container;
    }

    @Bean(name = "userTopicSubscriberDefaultMessageListenerContainer")
    public DefaultMessageListenerContainer UserTopicSubscriberDefaultMessageListenerContainer(
            @Autowired
            @Qualifier("topicConnectionFactory") ConnectionFactory connectionFactory,
            @Autowired
            @Qualifier("userTopicSubscriberReceiver") MessageListener messageListener,
            @Autowired
            @Qualifier("userTopicBean") Destination destination) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setPubSubDomain(true);
        container.setSubscriptionDurable(false);
        container.setConnectionFactory(connectionFactory);
        container.setupMessageListener(messageListener);
        container.setDestination(destination);
        return container;
    }

    @Component("userTopicReceiver")
    public class UserTopicReceiver implements MessageListener {

        @JmsListener(destination = TOPIC_NAME, containerFactory = "topicJmsListenerContainerFactory")
        public void onMessage(Message message) {
            System.out.println(String.format("-->onMessage() UserTopic: %s", message));
            try {
                if (message instanceof ActiveMQTextMessage) {
                    String json = ((ActiveMQTextMessage) message).getText();
                    if (json.startsWith("{") && json.endsWith("}")) {
                        ObjectMapper mapper = new ObjectMapper();
                        UserMessage userMessage = mapper.readValue(json, UserMessage.class);
                        System.out.println(String.format("--> UserTopic: %s", userMessage.toString()));
                        return;
                    }
                    System.out.println(String.format("--> UserTopic: %s", json));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Component("userTopicSubscriberReceiver")
    public class UserTopicSubscriberReceiver implements MessageListener {

        @JmsListener(destination = TOPIC_NAME, subscription = SUBSCRIBER_NAME)
        public void onMessage(Message message) {
            System.out.println(String.format("-->onMessage() UserTopicSubscriber: %s", message));
            try {
                if (message instanceof ActiveMQTextMessage) {
                    String json = ((ActiveMQTextMessage) message).getText();
                    if (json.startsWith("{") && json.endsWith("}")) {
                        ObjectMapper mapper = new ObjectMapper();
                        UserMessage userMessage = mapper.readValue(json, UserMessage.class);
                        System.out.println(String.format("--> UserTopicSubscriber: %s", userMessage.toString()));
                        return;
                    }
                    System.out.println(String.format("--> UserTopic: %s", json));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
