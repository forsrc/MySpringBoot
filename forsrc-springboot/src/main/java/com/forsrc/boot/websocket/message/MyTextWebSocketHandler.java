package com.forsrc.boot.websocket.message;


import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.text.MessageFormat;


public class MyTextWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = Logger.getLogger(MyTextWebSocketHandler.class);


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String username = MapUtils.getString(session.getAttributes(), "USERNAME");
        WebSocketClients.getInstance().getClients().put(username, session);
        LOGGER.info(MessageFormat.format("afterConnectionEstablished -> {0} : {1}", session, username));
        session.sendMessage(new TextMessage("afterConnectionEstablished ok"));
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String username = MapUtils.getString(session.getAttributes(), "USERNAME");
        WebSocketClients.getInstance().getClients().get(username).sendMessage(message);
        LOGGER.info(MessageFormat.format("handleTextMessage -> {0} : {1}", message, username));
        session.sendMessage(new TextMessage(MessageFormat.format("handleTextMessage -> {0} : {1}", message, username)));
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String username = MapUtils.getString(session.getAttributes(), "USERNAME");
        LOGGER.error(MessageFormat.format("websocket connection exception: {0}", username));
        LOGGER.error(exception.getMessage(), exception);

        WebSocketClients.getInstance().getClients().remove(username);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username = MapUtils.getString(session.getAttributes(), "USERNAME");
        WebSocketClients.getInstance().getClients().remove(username);
        LOGGER.info(MessageFormat.format("afterConnectionClosed -> {0} : {1}", session, username));

    }


    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
