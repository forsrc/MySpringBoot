package com.forsrc.boot.adminserver.web.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
//@RestController
//@RequestMapping("/test")
public class DemoController {

    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    //@ResponseBody
    @PreAuthorize("isAuthenticated()")
    String helloWorld() {
        System.out.println(String.format("Hello World! --> %s", new Date().toString()));
        return "/test";
    }
}
