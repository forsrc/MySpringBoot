package com.forsrc.boot.web.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        System.out.println("--> ExceptionController.defaultErrorHandler():");
        e.printStackTrace();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", e);
        modelAndView.addObject("url", req.getRequestURL());
        modelAndView.setViewName("/error");
        return modelAndView;
    }
}
