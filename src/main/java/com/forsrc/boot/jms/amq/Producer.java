package com.forsrc.boot.jms.amq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue queue;

    @Scheduled(fixedDelay = 60 * 1000)
    public void send() {
        QueueMessage qm = new QueueMessage();
        qm.setMessage(new Date().toString());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String message = mapper.writeValueAsString(qm);
            this.jmsMessagingTemplate.convertAndSend(this.queue, message);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("q/user");
    }
}
