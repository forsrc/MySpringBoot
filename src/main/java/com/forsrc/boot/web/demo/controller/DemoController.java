package com.forsrc.boot.web.demo.controller;

import java.util.Date;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
//@RestController
//@RequestMapping("/test")
public class DemoController {

    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    //@ResponseBody
    @PreAuthorize("authenticated and hasPermission('ROLE_TEST')")
    String helloWorld() {
        System.out.println(String.format("Hello World! --> %s", new Date().toString()));
        return "/test";
    }
}
