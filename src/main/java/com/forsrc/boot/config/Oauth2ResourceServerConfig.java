package com.forsrc.boot.config;


import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

//@EnableResourceServer
//@Configuration
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER + 1)
public class Oauth2ResourceServerConfig extends //WebSecurityConfigurerAdapter //
ResourceServerConfigurerAdapter
{

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/login").anonymous()
                .antMatchers(HttpMethod.POST, "/login").anonymous()
                .antMatchers(HttpMethod.GET, "/oauth2/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.POST, "/oauth2/**").access("#oauth2.hasScope('write')")
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        ;
    }


}