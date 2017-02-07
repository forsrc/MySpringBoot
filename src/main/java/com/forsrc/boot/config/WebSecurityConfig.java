package com.forsrc.boot.config;

import com.forsrc.boot.web.security.MyUserDetails;
import com.forsrc.boot.web.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//@Order(SecurityProperties.BASIC_AUTH_ORDER + 1)
@ComponentScan(basePackages = "org.thymeleaf.extras.springsecurity4")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

// @formatter:off
        http
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/home").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
                .permitAll();
        // @formatter:on
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("forsrc").password("forsrc").roles("ADMIN");
        auth.userDetailsService(myUserDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public UserDetailsService myUserDetailsService() {
        return new MyUserDetailsService();
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
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }
}