package com.forsrc.boot.web.streaming.service.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.forsrc.boot.web.streaming.service.StreamingService;
import com.forsrc.boot.web.streaming.service.streaming.SinkService;
import com.forsrc.boot.web.streaming.service.streaming.SourceService;

@Service
public class StreamingServiceImpl implements StreamingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamingServiceImpl.class);

    @Autowired
    private SourceService sourceService;

    @Autowired
    private SinkService sinkService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(String payload) {
      LOGGER.info("sending payload='{}' to topic='kafka/topic/test'", payload);
      kafkaTemplate.send("kafka/topic/test", payload);
    }

    @KafkaListener(topics = "kafka/topic/test")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
      LOGGER.info("received payload='{}'", consumerRecord.toString());
    }

    @Override
    public List<String> in() {

        return Arrays.asList(sinkService.getMessage().split(","));
    }

    @Override
    public void out(List<String> list) {
        sourceService.send(Arrays.toString(list.toArray()));
    }



}
