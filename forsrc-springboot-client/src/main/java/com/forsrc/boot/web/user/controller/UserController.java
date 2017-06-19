package com.forsrc.boot.web.user.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.forsrc.pojo.User;

@Controller
public class UserController {

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Value("${auth-resource-server:https://localhost:8076}")
    private String resourceUrl;

    @RequestMapping(path = "/userinfo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> info() {
        ResponseEntity<User> user = restTemplate.getForEntity(resourceUrl + "/api/userinfo", User.class);
        return user;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    
    public String logout(Principal principal) {
        return "/";
    }
}
