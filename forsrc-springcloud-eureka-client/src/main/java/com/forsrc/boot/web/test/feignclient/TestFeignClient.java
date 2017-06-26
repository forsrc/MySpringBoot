package com.forsrc.boot.web.test.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.forsrc.boot.web.test.feignclient.impl.TestFeignClientImpl;

@FeignClient(name = TestFeignClient.SERVICE_ID, fallback = TestFeignClientImpl.class)
public interface TestFeignClient {

    public static final String SERVICE_ID = "forsrc-springcloud-eureka-client/feignclient/user";
    String API_PATH = "/api/v1/test";

    @RequestMapping(value = API_PATH + "/confirm", method = RequestMethod.PUT, produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public <T> T confirm(@RequestBody T request);

    @RequestMapping(value = API_PATH + "/cancel", method = RequestMethod.PUT, produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public <T> T cancel(@RequestBody T request);
}
