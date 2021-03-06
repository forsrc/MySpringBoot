package com.forsrc.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import com.forsrc.core.web.security.MyAuthenticationHandler;
import com.forsrc.core.web.security.MyAuthenticationProvider;
import com.forsrc.core.web.security.MyUserDetailsService;

@EnableAuthorizationServer()
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
// @Order(SecurityProperties.BASIC_AUTH_ORDER + 1)
@ComponentScan(basePackages = "org.thymeleaf.extras.springsecurity4")
@Primary
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // @formatter:off
        http
                .authorizeRequests().antMatchers("/myhtmlshell/**").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/home").successHandler(myAuthenticationHandler()).permitAll()
                .and()
                .logout().addLogoutHandler(myAuthenticationHandler()).logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
                .permitAll()
        // oauth2
        //.and()
        //.requestMatchers()
        //.antMatchers("/", "/oauth/authorize", "/oauth/confirm_access")
                .and()
                .csrf().ignoringAntMatchers("/demo/**", "/myhtmlshell/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        ;
        // @formatter:on
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("forsrc").password("forsrc").roles("ADMIN");
        //auth.userDetailsService(myUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(myAuthenticationProvider());
    }

    @Bean
    public MyAuthenticationProvider myAuthenticationProvider() {
        return new MyAuthenticationProvider(myUserDetailsService());
    }

    @Bean
    public UserDetailsService myUserDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public MyAuthenticationHandler myAuthenticationHandler() {
        return new MyAuthenticationHandler();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        final HttpSecurity http = getHttp();
        web.postBuildAction(new Runnable() {
            @Override
            public void run() {
                web.securityInterceptor(http.getSharedObject(FilterSecurityInterceptor.class));
            }
        });
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}