package com.forsrc.boot.web.user.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    OAuth2RestTemplate restTemplate;
    @Value("${messages.url:https://localhost:8076}/api")
    String messagesUrl;

    @RequestMapping(path = "/info", method = RequestMethod.GET)
    public ResponseEntity<Principal>  me(Principal principal) {
        return new ResponseEntity<>(principal, HttpStatus.OK);
    }

}
