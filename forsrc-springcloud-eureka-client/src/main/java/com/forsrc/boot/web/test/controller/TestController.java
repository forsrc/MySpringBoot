package com.forsrc.boot.web.test.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.forsrc.boot.web.test.feignclient.TestFeignClient;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

@RestController
public class TestController {

    @Autowired
    private Environment environment;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private TestFeignClient testFeignClient;

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseEntity<List<List<ServiceInstance>>> info(@RequestParam Integer a, @RequestParam Integer b) {
        List<List<ServiceInstance>> list = new ArrayList<>();
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            list.add(discoveryClient.getInstances(service));
        }
        System.out.println("---> forsrc-springcloud-eureka-client");
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

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

    @RequestMapping(value = "/test/feignclient/cancel/{message}")
    public ResponseEntity<String> cancel(@PathVariable String message) {
        TestFeignClient feignClient = Feign.builder().encoder(new JacksonEncoder()).decoder(new JacksonDecoder())
                // .requestInterceptor(new BasicAuthRequestInterceptor("forsrc",
                // "forsrc"))
                .target(TestFeignClient.class, "http://localhost:8078");
        return new ResponseEntity<String>(feignClient.cancel(message), HttpStatus.OK);
    }

    @RequestMapping("/test/feignclient/{name}")
    public ResponseEntity<String> testFeignClient(@PathVariable("name") String name) {
        return new ResponseEntity<String>("forsrc-springcloud-eureka-client: " + name, HttpStatus.OK);
    }

    @RequestMapping("/test/consumer/{name}")
    public ResponseEntity<String> consumer(@PathVariable("name") String name) {
        return new ResponseEntity<String>("forsrc-springcloud-eureka-client: " + name, HttpStatus.OK);
    }
}
