package com.forsrc.boot.web.test.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.forsrc.boot.web.test.feignclient.impl.TestFeignClientImpl;

import feign.RequestLine;

@FeignClient(name = TestFeignClient.SERVICE_ID, fallback = TestFeignClientImpl.class)
@RequestMapping(value = TestFeignClient.API_PATH)
public interface TestFeignClient {

    public static final String SERVICE_ID = "forsrc-springcloud-eureka-client";
    public static final String API_PATH = "/api/v1/test";

    @RequestMapping(value = "/confirm", method = RequestMethod.PUT, produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    @RequestLine("PUT /api/v1/test/confirm/{request}")
    public <T> T confirm(@PathVariable("request") T request);

    @RequestMapping(value = "/cancel/{request}", method = { RequestMethod.DELETE}, produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    @RequestLine("GET /api/v1/test/cancel/{request}")
    public <T> T cancel(@PathVariable("request") T request);
}
