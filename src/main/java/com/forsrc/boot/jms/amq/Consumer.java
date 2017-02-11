package com.forsrc.boot.jms.amq;

import org.springframework.jms.annotation.JmsListener;

//@Component
public class Consumer {

    @JmsListener(destination = "q_user")
    public void receiveQueue(String text) {
        System.out.println(String.format("--> Consumer.receiveQueue() -> %s", text));
    }

    @JmsListener(destination = "t_user")
    public void receiveTopic(String text) {
        System.out.println(String.format("--> Consumer.receiveTopic() -> %s", text));
    }
}
