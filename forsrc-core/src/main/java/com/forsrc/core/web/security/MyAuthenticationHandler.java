package com.forsrc.core.web.security;

import com.forsrc.core.web.user.service.UserService;
import com.forsrc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MyAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler
        implements AuthenticationFailureHandler, LogoutHandler {

    RequestCache requestCache = new HttpSessionRequestCache();
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {


        HttpSession session = request.getSession();
        SecurityContext sc = SecurityContextHolder.getContext();
        Object principal = sc.getAuthentication().getPrincipal();
        if(principal instanceof MyUserDetails){
        MyUserDetails myUserDetails = (MyUserDetails) sc.getAuthentication().getPrincipal();
            User user = userService.get(myUserDetails.getUserPrivacy().getUserId());
            session.setAttribute("USER", user);
        }
        System.out.println(String.format("--> onAuthenticationSuccess(): %s", sc.getAuthentication().getName()));
      
        //clearAuthenticationAttributes(request);

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        String targetUrl = savedRequest.getRedirectUrl();
        if (targetUrl != null && "".equals(targetUrl)) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ae) throws IOException, ServletException {
        SecurityContext sc = SecurityContextHolder.getContext();
        System.out.println(String.format("--> onAuthenticationFailure(): %s", sc.getAuthentication().getName()));
        SecurityContextHolder.clearContext();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContext sc = SecurityContextHolder.getContext();
        if (sc == null) {
            return;
        }
        System.out.println(String.format("--> logout(): %s", sc.getAuthentication().getName()));
        SecurityContextHolder.clearContext();
        clearAuthenticationAttributes(request);
    }
}
