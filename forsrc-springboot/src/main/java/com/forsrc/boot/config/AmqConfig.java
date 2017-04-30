package com.forsrc.boot.config;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class AmqConfig {

    @Resource(name = "queueConnectionFactory")
    private ConnectionFactory queueConnectionFactory;
    @Resource(name = "topicConnectionFactory")
    private ConnectionFactory topicConnectionFactory;

    @Bean(name = "queueConnectionFactory")
    @Primary
    public ConnectionFactory queueConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Config.queueHost);
        return connectionFactory;
    }

    @Bean(name = "topicConnectionFactory")
    public ConnectionFactory topicConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Config.topicHost);
        return connectionFactory;
    }

    @Bean(name = "queueJmsTemplate")
    @Primary
    public JmsTemplate queueJmsTemplate() {
        // ConnectionFactory queueConnectionFactory = new
        // ActiveMQConnectionFactory(queueHost);
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(queueConnectionFactory);
        jmsTemplate.setPubSubDomain(false);
        return jmsTemplate;
    }

    @Bean(name = "topicJmsTemplate")
    public JmsTemplate topicJmsTemplate() {
        // ConnectionFactory topicConnectionFactory = new
        // ActiveMQConnectionFactory(topicHost);
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(topicConnectionFactory);
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }

    @Bean(name = "queueJmsListenerContainerFactory")
    public JmsListenerContainerFactory<?> queueJmsListenerContainerFactory(
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(false);
        // This provides all boot's default to this factory, including the
        // message converter
        configurer.configure(factory, queueConnectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    @Bean(name = "topicJmsListenerContainerFactory")
    public JmsListenerContainerFactory<?> topicJmsListenerContainerFactory(
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        // This provides all boot's default to this factory, including the
        // message converter
        configurer.configure(factory, topicConnectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    @Configuration
    @ConfigurationProperties(prefix = "amq")
    public static class Config {

        public static String host;
        public static String queueHost;
        public static String topicHost;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            Config.host = host;
        }

        public String getQueueHost() {
            return queueHost;
        }

        public void setQueueHost(String queueHost) {
            Config.queueHost = queueHost;
        }

        public String getTopicHost() {
            return topicHost;
        }

        public void setTopicHost(String topicHost) {
            Config.topicHost = topicHost;
        }
    }
    // @Bean // Serialize message content to json using TextMessage
    // public MessageConverter jacksonJmsMessageConverter() {
    // MappingJackson2MessageConverter converter = new
    // MappingJackson2MessageConverter();
    // //converter.setTargetType(MessageType.TEXT);
    // //converter.setTypeIdPropertyName("_type");
    // return converter;
    // }
}