package com.forsrc.boot.web.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forsrc.boot.web.test.feignclient.TestFeignClient;

@RestController
public class TestController {

    @Autowired
    private Environment environment;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private TestFeignClient testFeignClient;

    @RequestMapping("/client/getInstances/{name}")
    public ResponseEntity<List<ServiceInstance>> getInstances(@PathVariable String name) {
        return new ResponseEntity<>(this.discoveryClient.getInstances(name), HttpStatus.OK);
    }

    @RequestMapping("/client/getServices")
    public ResponseEntity<List<String>> getServices() {
        return new ResponseEntity<>(this.discoveryClient.getServices(), HttpStatus.OK);
    }

    @RequestMapping(value = "/test")
    public ResponseEntity<String> test(String name) {

        return new ResponseEntity<>("hello world. " + (name == null ? "" : name), HttpStatus.OK);
    }

    @RequestMapping(value = "/evn/{key}")
    public ResponseEntity<String> evn(@PathVariable("key") String key) {
        return new ResponseEntity<>("Environment: " + environment.getProperty(key), HttpStatus.OK);
    }

    @RequestMapping(value = "/test/feignclient/confirm/{message}")
    public ResponseEntity<String> confirm(@PathVariable String message) {
        return new ResponseEntity<String>(testFeignClient.confirm(message), HttpStatus.OK);
    }

}
