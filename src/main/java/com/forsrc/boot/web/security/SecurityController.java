package com.forsrc.boot.web.security;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SecurityController {

    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public ModelAndView login() throws Exception {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/home", method = {RequestMethod.GET})
    public ModelAndView home() throws Exception {
        return new ModelAndView("home");
    }

}
