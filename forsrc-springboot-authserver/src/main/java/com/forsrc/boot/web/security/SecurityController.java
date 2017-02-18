package com.forsrc.boot.web.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SecurityController {

    @RequestMapping(value = "/login")
    public String login() throws Exception {
        return "/login";
    }

    @RequestMapping(value = "/404")
    public ModelAndView e404(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        String url = String.format("%s://%s:%s%s%s"
                , request.getScheme()
                , request.getServerName()
                , request.getServerPort()
                , request.getAttribute("javax.servlet.forward.request_uri")
                , request.getQueryString() != null ? "?" + request.getQueryString() : "");
        modelAndView.addObject("url", url);
        modelAndView.setViewName("/404");
        return modelAndView;
    }

    @RequestMapping(value = "/403")
    public ModelAndView e403(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        String url = String.format("%s://%s:%s%s%s"
                , request.getScheme()
                , request.getServerName()
                , request.getServerPort()
                , request.getAttribute("javax.servlet.forward.request_uri")
                , request.getQueryString() != null ? "?" + request.getQueryString() : "");
        modelAndView.addObject("url", url);
        modelAndView.setViewName("/403");
        return modelAndView;
    }

    @RequestMapping(value = "/502")
    public ModelAndView e502(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        String url = String.format("%s://%s:%s%s%s"
                , request.getScheme()
                , request.getServerName()
                , request.getServerPort()
                , request.getAttribute("javax.servlet.forward.request_uri")
                , request.getQueryString() != null ? "?" + request.getQueryString() : "");
        modelAndView.addObject("url", url);
        modelAndView.setViewName("/502");
        return modelAndView;
    }

    @RequestMapping(value = "/home")
    public ModelAndView home() throws Exception {
        return new ModelAndView("/home");
    }

}
