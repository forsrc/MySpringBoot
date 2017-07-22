package com.forsrc.boot.web.test.feignclient.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.forsrc.boot.web.test.feignclient.service.ConsumerService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ConsumerServiceImp implements ConsumerService{

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "fallback")
    public String consumer() {

        return restTemplate.getForEntity("http://FORSRC-SPRINGCLOUD-EUREKA-CLIENT/test/consumer/TestConsumer", String.class).getBody();
    }

    @Override
    public String fallback() {
        return "forsrc-springcloud-eureka-ribbon: fallback";
    }

    @Override
    @HystrixCommand(fallbackMethod = "noConsumerFallback")
    public String noConsumer() {
        return restTemplate.getForEntity("http://FORSRC-SPRINGCLOUD-EUREKA-CLIENT/test/noConsumer/TestNoConsumer", String.class).getBody();
    }

    @Override
    public String noConsumerFallback() {
        return "forsrc-springcloud-eureka-ribbon: noConsumerFallback";
    }

}
