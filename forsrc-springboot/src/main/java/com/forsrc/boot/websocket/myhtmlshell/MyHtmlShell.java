package com.forsrc.boot.websocket.myhtmlshell;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MyHtmlShell {

    private CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

    @RequestMapping(value = "/myhtmlshell", method = { RequestMethod.GET })
    public ModelAndView html() {
        return new ModelAndView("myhtmlshell/MyHtmlShell");
    }

    @RequestMapping(value = "/myhtmlshell/open", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public String open() {
        String uuid = UUID.randomUUID().toString();
        list.add(uuid);
        System.out.println("open --> " + uuid);
        return uuid;
    }

    @MessageMapping("/msg")//  /app-myhtmlshell/msg
    @SendTo("/topic/myhtmlshell")
    public String send(String message) throws Exception {

        return message;
    }
}
