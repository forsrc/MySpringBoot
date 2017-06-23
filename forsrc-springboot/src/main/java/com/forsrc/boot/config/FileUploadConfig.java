package com.forsrc.boot.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.annotation.MultipartConfig;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
@MultipartConfig(location = "/tmp", fileSizeThreshold = 32768, maxFileSize = 5242880, maxRequestSize = 27262976)
@EnableAutoConfiguration(exclude={MultipartAutoConfiguration.class})
@Order(-100000)
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("100MB");
        factory.setMaxRequestSize("100MB");
        return factory.createMultipartConfig();
    }

    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver multipartResolver() {
        //CommonsMultipartResolver multipart = new CommonsMultipartResolver();
        //multipart.setMaxUploadSize(100 * 1024 * 1024);
        StandardServletMultipartResolver multipart = new StandardServletMultipartResolver();
        return multipart;
    }

    @Bean

    public MultipartFilter multipartFilter() {
        MultipartFilter multipartFilter = new MultipartFilter();
        multipartFilter.setMultipartResolverBeanName("multipartReso‌‌​​lver");
        return multipartFilter;
    }
}
