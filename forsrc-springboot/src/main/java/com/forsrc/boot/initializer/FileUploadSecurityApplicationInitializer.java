package com.forsrc.boot.initializer;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.multipart.support.MultipartFilter;

@Configuration
public class FileUploadSecurityApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    public FileUploadSecurityApplicationInitializer() {
        System.out.println("[Initializer] ------------> FileUploadSecurityApplicationInitializer");
    }

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {

        insertFilters(servletContext, new MultipartFilter());
    }
}