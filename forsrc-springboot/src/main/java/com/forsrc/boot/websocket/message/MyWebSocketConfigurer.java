package com.forsrc.boot.websocket.message;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class MyWebSocketConfigurer implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(new MyWebSocketHandler(), "/websocket").addInterceptors(new WebSocketHandshakeInterceptor());

        webSocketHandlerRegistry.addHandler(new MyWebSocketHandler(), "/websocket/sockjs").addInterceptors(new WebSocketHandshakeInterceptor())
                .withSockJS();
    }
}
