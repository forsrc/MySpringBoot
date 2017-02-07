package com.forsrc.boot.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.ServerEndpointConfig;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebsocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/ws");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket/stomp").withSockJS();
        registry.addEndpoint("/websocket/ws").withSockJS();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    //@Component
    public static class ServerEndpointConfigurator extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

        /**
         * Spring application context.
         */
        private static volatile BeanFactory context;

        @Override
        public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
            return context.getBean(clazz);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            ServerEndpointConfigurator.context = applicationContext;
        }

    }
}
