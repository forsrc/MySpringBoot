package com.forsrc.boot.web.user.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.forsrc.core.web.security.MyUserDetails;
import com.forsrc.core.web.user.service.UserService;
import com.forsrc.pojo.User;

@RestController
public class UserController {
    
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/api/userinfo", method = RequestMethod.GET)
    public ResponseEntity<User> me(Principal principal) {
        if (principal instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)principal;
            MyUserDetails myUserDetails = (MyUserDetails) oAuth2Authentication.getUserAuthentication().getPrincipal();
            User user = userService.get(myUserDetails.getUserPrivacy().getUserId());
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>((User)null, HttpStatus.OK);
    }
}
