package com.forsrc.boot.jms.amq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.Message;
import javax.jms.MessageListener;

public class Receiver implements MessageListener {

    @JmsListener(destination = "myqueue")
    public void onMessage(Message message) {
        try {
            if (message instanceof ActiveMQTextMessage) {
                String json = ((ActiveMQTextMessage) message).getText();
                ObjectMapper mapper = new ObjectMapper();
                QueueMessage queueMessage = mapper.readValue(json, QueueMessage.class);
                System.out.println("QueueMessage: " + queueMessage.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}