package com.forsrc.boot.jms.amq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @JmsListener(destination = "q/user")
    public void receiveQueue(String text) {
        System.out.println("Consumer -> receiveQueue ->" + text);
    }
}
