package com.forsrc.boot.websocket.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserWebsocketController implements Runnable {

    private Thread thread;
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/user")
    @SendTo("/topic/user")
    public UserWebsocketMessage oMessage(UserWebsocketMessage message) throws Exception {
        System.out.println("--> oMessage(): " + message.getName());
        template.convertAndSend("/topic/user", new UserWebsocketMessage(-4L, "" + System.currentTimeMillis()));
        return new UserWebsocketMessage(-1L, "Hello, " + message.getName() + "!");
    }

    @RequestMapping("/thread")
    @ResponseBody
    public UserWebsocketMessage thread() {
        thread = new Thread(this);
        thread.start();
        return new UserWebsocketMessage(-2L, "thread");
    }

    @Override
    public void run() {
        for (int i = 10; i < 10; i++) {
            template.convertAndSend("/topic/user", new UserWebsocketMessage(-3L, "" + System.currentTimeMillis()));
        }
    }
}