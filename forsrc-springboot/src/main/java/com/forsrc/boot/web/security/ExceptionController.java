package com.forsrc.boot.web.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.h2.jdbc.JdbcSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = JdbcSQLException.class)
    public ModelAndView jdbcSQLException(HttpServletRequest req, JdbcSQLException e) throws Exception {
        System.out.println("--> JdbcSQLException.jdbcSQLException():");
        e.printStackTrace();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", e);
        modelAndView.addObject("url", req.getRequestURL());
        if ("GET".equals(req.getMethod())) {
            modelAndView.setViewName("/error");
        }
        return modelAndView;
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, Object>> exception(HttpServletRequest req, Exception e) throws Exception {
        System.out.println("--> ExceptionHandler.exception():");
        e.printStackTrace();
        Map<String, Object> map = new HashMap<>();
        map.put("exception", e.getMessage());
        map.put("url", req.getRequestURL());
        map.put("method",  req.getMethod());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
