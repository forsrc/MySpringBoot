package com.forsrc.boot.web.test.feignclient.impl;

import org.springframework.stereotype.Component;

import com.forsrc.boot.web.test.feignclient.TestFeignClient;

@Component
public class TestFeignClientImpl implements TestFeignClient {

    @Override
    public <T> T confirm(T request) {
        System.out.println(String.format("[FeignClient] %s confirm() --> %s", SERVICE_ID, request));
        return request;
    }

    @Override
    public <T> T cancel(T request) {
        System.out.println(String.format("[FeignClient] %s cancel() --> %s", SERVICE_ID, request));
        return request;
    }

}
