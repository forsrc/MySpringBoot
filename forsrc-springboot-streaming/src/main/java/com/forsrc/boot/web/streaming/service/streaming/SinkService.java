package com.forsrc.boot.web.streaming.service.streaming;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@EnableBinding(Sink.class)
@Service
public class SinkService {

    private String message;

    @StreamListener(Sink.INPUT)
    public void process(Message<?> message) {
        this.message = message.getPayload().toString();
        System.out.println(message.getPayload());
        Acknowledgment acknowledgment = message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
        if (acknowledgment != null) {
            System.out.println("Acknowledgment provided");
            acknowledgment.acknowledge();
        }
    }

    public synchronized String getMessage() {
        return message;
    }
}
