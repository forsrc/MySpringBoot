package com.forsrc.boot.web.test.feignclient.service;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public interface ConsumerService {

    @HystrixCommand(fallbackMethod = "fallback")
    public String consumer();

    public String fallback();

    @HystrixCommand(fallbackMethod = "noConsumerFallback")
    public String noConsumer();

    public String noConsumerFallback();
}
