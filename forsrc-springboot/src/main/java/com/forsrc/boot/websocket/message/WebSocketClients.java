package com.forsrc.boot.websocket.message;


import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public class WebSocketClients {

    private WebSocketClients() {

    }

    public static WebSocketClients getInstance() {
        return MyWebSocketClients.INSTANCE;
    }

    public WebSocketSession getWebSocketSession(String username) {
        return getClients().get(username);
    }

    public Map<String, WebSocketSession> getClients() {
        return MyWebSocketClients.CLIENTS;
    }

    private static class MyWebSocketClients {
        private static final Map<String, WebSocketSession> CLIENTS = new HashMap<String, WebSocketSession>();
        private static WebSocketClients INSTANCE = new WebSocketClients();
    }

}
