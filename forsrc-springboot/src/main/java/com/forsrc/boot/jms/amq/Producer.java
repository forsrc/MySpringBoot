package com.forsrc.boot.jms.amq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.Topic;
import java.util.Date;

//@Component
public class Producer {

    @Resource(name = "topicJmsTemplate")
    private JmsTemplate topicJmsTemplate;
    @Resource(name = "queueJmsTemplate")
    private JmsTemplate queueJmsTemplate;

    @Resource(name = "queue")
    private Queue queue;
    @Resource(name = "topic")
    private Topic topic;

    @Scheduled(fixedDelay = 60 * 1000)
    public void send() {
        String message = new Date().toString();
        this.queueJmsTemplate.convertAndSend(this.queue, message);
        this.topicJmsTemplate.convertAndSend(this.topic, message);
    }

    @Bean(name = "queue")
    public Queue queue() {
        return new ActiveMQQueue("q_user");
    }

    @Bean(name = "topic")
    public Topic topic() {
        return new ActiveMQTopic("t_user");
    }
}
