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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.forsrc.boot.web.test.feignclient.TestFeignClient;
import com.forsrc.boot.web.test.feignclient.service.ConsumerService;

@RestController
public class TestController {

    @Autowired
    private Environment environment;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private TestFeignClient testFeignClient;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ConsumerService consumerService;

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseEntity<List<List<ServiceInstance>>> info() {
        List<List<ServiceInstance>> list = new ArrayList<>();
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            list.add(discoveryClient.getInstances(service));
        }
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

    @RequestMapping("/test/feignclient/{name}")
    public ResponseEntity<String> testFeignClient(@PathVariable("name") String name) {
        System.out.println("---> forsrc-springcloud-eureka-ribbon");
        return testFeignClient.testFeignClient(name);
    }

    @RequestMapping("/test/ribbon/{name}")
    public ResponseEntity<String> testRiboonClient(@PathVariable("name") String name) {
        System.out.println("---> forsrc-springcloud-eureka-ribbon");
        return restTemplate.getForEntity("http://FORSRC-SPRINGCLOUD-EUREKA-CLIENT/test/feignclient/" + name, String.class);
    }

    @RequestMapping("/test/consumer")
    public ResponseEntity<String> consumer() {
        System.out.println("---> forsrc-springcloud-eureka-ribbon.consumer()");
        return new ResponseEntity<>(consumerService.consumer(), HttpStatus.OK);
    }

    @RequestMapping("/test/noConsumer")
    public ResponseEntity<String> noConsumer() {
        System.out.println("---> forsrc-springcloud-eureka-ribbon.noConsumer()");
        return new ResponseEntity<>(consumerService.noConsumer(), HttpStatus.OK);
    }
}
