package com.forsrc.boot.adminserver.web.user.controller;

import java.security.Principal;

import com.forsrc.core.web.security.MyUserDetails;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping(path = "/me")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public Principal user(Principal user) {
        if(!(user instanceof UsernamePasswordAuthenticationToken)){
            return user;
        }
        UsernamePasswordAuthenticationToken upat = (UsernamePasswordAuthenticationToken) user;
        Object principal = upat.getPrincipal();
        if (principal instanceof MyUserDetails){
            MyUserDetails myUserDetails = (MyUserDetails)principal;
            myUserDetails.getUserPrivacy().setPassword(myUserDetails.getPassword());
        }

        return user;
    }
}
