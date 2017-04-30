package com.forsrc.boot.web.init.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.forsrc.boot.web.init.service.InitService;

//@RestController
@Controller
@RequestMapping("/init")
public class InitController {

    @Resource
    private InitService initService;

    @RequestMapping(value = "/db", method = { RequestMethod.GET })
    @ResponseBody
    // @PreAuthorize("isAuthenticated()")
    String initDb() throws Exception {
        initService.initDb();
        return "initDb() --> ok";
    }
}
