package com.forsrc.boot.jms.amq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @JmsListener(destination = "q_user")
    public void receiveQueue(String text) {
        System.out.println("--> Consumer.receiveQueue() -> " + text);
    }

    @JmsListener(destination = "t_user")
    public void receiveTopic(String text) {
        System.out.println("--> Consumer.receiveTopic() -> " + text);
    }
}
