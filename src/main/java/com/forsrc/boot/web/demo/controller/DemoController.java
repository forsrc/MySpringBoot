package com.forsrc.boot.web.demo.controller;

import java.util.Date;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
@RequestMapping("/test")
public class DemoController {

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    @ResponseBody
    String helloWorld() {
        System.out.println("Hello World! --> " + new Date().toString());
        return "Hello World! --> " + new Date().toString();
    }
}
