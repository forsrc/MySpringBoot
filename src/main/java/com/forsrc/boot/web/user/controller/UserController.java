package com.forsrc.boot.web.user.controller;

import com.forsrc.boot.web.user.service.UserService;
import com.forsrc.pojo.User;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = {RequestMethod.GET})
    @ResponseBody
    public List<User> get() throws Exception{
        try {
            List<User> list = userService.get(0, 10);
            //System.out.println("---> " + ((Object[])(list.get(0)))[0]);
            return list;
        } catch (Exception e) {
            throw new Exception(MessageFormat.format("Exception --> {0} : {1}", new Date().toString(), e.getMessage()));
        }
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET})
    @ResponseBody
    public User add(String username, String password, String email) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            userService.save(user, password.toCharArray());
            return user;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequestMapping(value = "/", method = {RequestMethod.PUT})
    @ResponseBody
    public User save(String username, String email) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            userService.save(user);
            return user;
        } catch (Exception ex) {
            throw ex;
        }

    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseBody
    public User delete(@PathVariable("id") Long id) {
        System.out.println(MessageFormat.format("delete({0}) --> {1}", id, new Date().toString()));
        try {
            User user = new User(id);
            userService.delete(user);
            return user;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    @ResponseBody
    public User get(@PathVariable("id") Long id) {
        System.out.println(MessageFormat.format("get({0}) --> {1}", id, new Date().toString()));
        User user = userService.get(id);

        return user;
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    @ResponseBody
    public User update(@PathVariable("id") Long id, String username, String email) {
        System.out.println(MessageFormat.format("update({0}) --> {1}", id, new Date().toString()));
        try {
            User user = userService.get(id);
            user.setEmail(email);
            user.setUsername(username);
            userService.update(user);
            return user;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
