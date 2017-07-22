package com.forsrc.boot.web.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class TestController {

    @Autowired
    private Environment environment;

    @RequestMapping(value = "/test")
    public ResponseEntity<String> test(String name) {

        return new ResponseEntity<>("hello world. " + (name == null ? "" : name), HttpStatus.OK);
    }

    @RequestMapping(value = "/evn/{key}")
    public ResponseEntity<String> evn(@PathVariable("key") String key) {
        return new ResponseEntity<>("Environment: " + environment.getProperty(key), HttpStatus.OK);
    }

    @RequestMapping(value = "/test/hystrix/{name}")
    @HystrixCommand(fallbackMethod = "hystrixFallBack")
    public ResponseEntity<String> hystrix(@PathVariable("name") String name) {

        return new ResponseEntity<>("hello world. " + (name == null ? "" : name), HttpStatus.OK);
    }
    public ResponseEntity<String> hystrixFallBack(String name) {

        return new ResponseEntity<>("forsrc-springcloud-hystrix: hystrixFallBack -> " + (name == null ? "" : name), HttpStatus.OK);
    }
}
