package com.forsrc.core.web.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class MyAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService myUserDetailsService;

    public MyAuthenticationProvider(UserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        if (password == null) {
            throw new BadCredentialsException(String.format("Null password for: %s", username));
        }
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        String md5 = DigestUtils.md5Hex(String.format("%s/%s", username, password));
        if (!md5.equals(userDetails.getPassword())) {
            throw new BadCredentialsException(String.format("Invalid password for: %s", username));
        }
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
