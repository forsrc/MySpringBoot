package com.forsrc.boot.jms.amq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.Message;
import javax.jms.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver implements MessageListener {

    @JmsListener(destination = "q/user")
    public void onMessage(Message message) {
        try {
            if (message instanceof ActiveMQTextMessage) {
                String json = ((ActiveMQTextMessage) message).getText();
                if (json.startsWith("{") && json.endsWith("}")) {
                    ObjectMapper mapper = new ObjectMapper();
                    QueueMessage queueMessage = mapper.readValue(json, QueueMessage.class);
                    System.out.println("--> QueueMessage: " + queueMessage.toString());
                    return;
                }
                System.out.println("--> QueueMessage: " + json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}