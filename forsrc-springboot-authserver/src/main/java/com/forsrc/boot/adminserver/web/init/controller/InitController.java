package com.forsrc.boot.adminserver.web.init.controller;

import com.forsrc.boot.adminserver.web.init.service.InitService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

//@RestController
@Controller
@RequestMapping("/init")
public class InitController {

    @Resource
    private InitService initService;

    @RequestMapping(value = "/db", method = {RequestMethod.GET})
    @ResponseBody
        //@PreAuthorize("isAuthenticated()")
    String initDb() throws Exception {
        initService.initDb();
        return "initDb() --> ok";
    }
}
