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

@Component
public class UserTopic {

    public static final String TOPIC_NAME = "t/user";
    public static final String SUBSCRIBER_NAME = "s/user";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name = "userTopicBean")
    private Topic userTopicBean;

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


    @Component
    public class UserTopicReceiver implements MessageListener {
        @JmsListener(destination = TOPIC_NAME)
        public void onMessage(Message message) {
            try {
                if (message instanceof ActiveMQTextMessage) {
                    String json = ((ActiveMQTextMessage) message).getText();
                    if (json.startsWith("{") && json.endsWith("}")) {
                        ObjectMapper mapper = new ObjectMapper();
                        UserMessage userMessage = mapper.readValue(json, UserMessage.class);
                        System.out.println("--> UserTopic: " + userMessage.toString());
                        return;
                    }
                    System.out.println("--> UserTopic: " + json);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Component
    public class UserTopicSubscriberReceiver implements MessageListener {
        @JmsListener(destination = SUBSCRIBER_NAME)
        public void onMessage(Message message) {
            System.out.println("--> UserTopic: " + message);
            try {
                if (message instanceof ActiveMQTextMessage) {
                    String json = ((ActiveMQTextMessage) message).getText();
                    if (json.startsWith("{") && json.endsWith("}")) {
                        ObjectMapper mapper = new ObjectMapper();
                        UserMessage userMessage = mapper.readValue(json, UserMessage.class);
                        System.out.println("--> UserTopic: " + userMessage.toString());
                        return;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
