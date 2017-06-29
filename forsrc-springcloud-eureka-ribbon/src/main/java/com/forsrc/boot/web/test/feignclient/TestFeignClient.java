package com.forsrc.boot.web.test.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "FORSRC-SPRINGCLOUD-EUREKA-CLIENT")
public interface TestFeignClient {

    @RequestMapping(value = "/test/feignclient/{name}", method = { RequestMethod.GET }, produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE }, consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    // @RequestLine("GET /test/feignclient/{name}")
    // @RequestMapping(value = "/test/feignclient/{name}", method = {
    // RequestMethod.GET })
    public ResponseEntity<String> testFeignClient(@PathVariable("name") String name);
}
