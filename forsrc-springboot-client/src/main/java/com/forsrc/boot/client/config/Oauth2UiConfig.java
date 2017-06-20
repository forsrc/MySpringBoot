package com.forsrc.boot.client.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CompositeFilter;

@Configuration
@EnableOAuth2Client
//@EnableOAuth2Sso
public class Oauth2UiConfig //extends WebSecurityConfigurerAdapter
{

    @Autowired
    @Qualifier("oauth2RestTemplate")
    private OAuth2RestTemplate oauth2RestTemplate;

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Bean
    @Primary
    OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
            OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

    //@Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .antMatcher("/**")
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/", "/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .logout()
                .logoutSuccessUrl("/").permitAll()
            .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        ;
        // @formatter:on
    }

    private Filter ssoFilter() {

        CompositeFilter filter = new CompositeFilter();
        List<AbstractAuthenticationProcessingFilter> filters = new ArrayList<>();

        OAuth2ClientAuthenticationProcessingFilter forsrcFilter = new OAuth2ClientAuthenticationProcessingFilter(
                "/login");
        OAuth2RestTemplate facebookTemplate = new OAuth2RestTemplate(forsrcAuthorizationCodeResourceDetails(),
                oauth2ClientContext);
        forsrcFilter.setRestTemplate(facebookTemplate);

        UserInfoTokenServices tokenServices = new UserInfoTokenServices(forsrcResource().getUserInfoUri(),
                forsrcAuthorizationCodeResourceDetails().getClientId());
        tokenServices.setRestTemplate(facebookTemplate);

        forsrcFilter.setTokenServices(tokenServices);

        filters.add(forsrcFilter);
        filter.setFilters(filters);

        return filter;
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    @ConfigurationProperties("forsrc.client")
    public AuthorizationCodeResourceDetails forsrcAuthorizationCodeResourceDetails() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @Primary
    @ConfigurationProperties("forsrc.resource")
    public ResourceServerProperties forsrcResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}
