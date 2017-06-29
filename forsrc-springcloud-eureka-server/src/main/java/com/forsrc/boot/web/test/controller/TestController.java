package com.forsrc.boot.web.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private Environment environment;

    @RequestMapping(value = "/test")
    public ResponseEntity<String> test(String name) {

        return new ResponseEntity<>("hello world. " + (name == null ? "" : name), HttpStatus.OK);
    }

    @RequestMapping(value = "/evn")
    public ResponseEntity<String> evn(String key) {
        return new ResponseEntity<>("Environment: " + environment.getProperty(key), HttpStatus.OK);
    }

    @RequestMapping("/test/feignclient/{name}")
    public ResponseEntity<String> testFeignClient(@PathVariable String name) {
        return new ResponseEntity<>("server: " + name, HttpStatus.OK);
    }
}
