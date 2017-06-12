package com.forsrc.boot.web.oauth2.message.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    final List<Message> messages = Collections.synchronizedList(new LinkedList<Message>());

    @RequestMapping(path = "/api/messages", method = RequestMethod.GET)
    List<Message> getMessages(Principal principal) {
        return messages;
    }

    @RequestMapping(path = "/api/messages", method = RequestMethod.POST)
    Message postMessage(Principal principal, @RequestBody Message message) {
        message.username = principal.getName();
        message.createdAt = new Date();
        messages.add(0, message);
        return message;
    }

    public static class Message {
        public String text;
        public String username;
        public Date createdAt;
    }

}
