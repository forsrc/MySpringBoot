package com.forsrc.boot.config;

import com.forsrc.boot.jms.amq.Receiver;
import com.forsrc.boot.jms.amq.Sender;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

@Configuration
@ConfigurationProperties(prefix="amq")
public class AmqConfig {

    private String host;

    @Bean()
    ConnectionFactory connectionFactory(){
        return new ActiveMQConnectionFactory(host);
    }

    @Bean
    Sender sender(){
        return new Sender();
    }

    @Bean
    Receiver receiver(){
        return new Receiver();
    }

    public void setHost(String host) { this.host = host; }
}