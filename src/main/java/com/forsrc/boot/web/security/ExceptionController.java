package com.forsrc.boot.web.security;

import org.h2.jdbc.JdbcSQLException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = JdbcSQLException.class)
    public ModelAndView jdbcSQLException(HttpServletRequest req, JdbcSQLException e) throws Exception {
        System.out.println("--> JdbcSQLException.jdbcSQLException():");
        e.printStackTrace();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", e);
        modelAndView.addObject("url", req.getRequestURL());
        modelAndView.setViewName("/error");
        return modelAndView;
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView exception(HttpServletRequest req, Exception e) throws Exception {
        System.out.println("--> ExceptionHandler.exception():");
        e.printStackTrace();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", e);
        modelAndView.addObject("url", req.getRequestURL());
        modelAndView.setViewName("/error");
        return modelAndView;
    }
}
