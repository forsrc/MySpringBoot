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
    String get() {
        try {
            List<User> list = userService.get(0, 10);
            return list.toString();
        } catch (Exception e) {
            return MessageFormat.format("Exception --> {0} : {1}", new Date().toString(), e.getMessage());
        }
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET})
    @ResponseBody
    public String add(String username, String password, String email) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            userService.save(user);
        } catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }
        return "User succesfully created!";
    }

    @RequestMapping(value = "/", method = {RequestMethod.PUT})
    @ResponseBody
    public String save(String username, String password, String email) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            userService.save(user);
        } catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }
        return "User succesfully created!";
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    @ResponseBody
    public String delete(@PathVariable("id") Long id) {
        System.out.println(MessageFormat.format("delete({0}) --> {1}", id, new Date().toString()));
        try {
            User user = new User(id);
            userService.delete(user);
        } catch (Exception ex) {
            return "Error deleting the user: " + ex.toString();
        }
        return "User succesfully deleted!";
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    @ResponseBody
    public String get(@PathVariable("id") Long id) {
        System.out.println(MessageFormat.format("get({0}) --> {1}", id, new Date().toString()));
        User user = userService.get(id);

        return user == null ? "" : user.toString();
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    @ResponseBody
    public String update(@PathVariable("id") Long id, String username, String email) {
        System.out.println(MessageFormat.format("update({0}) --> {1}", id, new Date().toString()));
        try {
            User user = userService.get(id);
            user.setEmail(email);
            user.setUsername(username);
            userService.update(user);
        } catch (Exception ex) {
            return "Error updating the user: " + ex.toString();
        }
        return "User succesfully updated!";
    }
}
