package com.forsrc.boot.web.home.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/home")
    public String login() throws Exception {
        return "/home";
    }
}
