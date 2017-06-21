package com.forsrc.boot.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        //// @formatter:off
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.forsrc.boot.web"))
                .paths(PathSelectors.any())
                //.paths(PathSelectors.ant("/*"))
                .build()
                .apiInfo(apiInfo())
                ;
        // @formatter:on
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
          "My SpringBoot API",
          "Some custom description of API.",
          "API TOS",
          "Terms of service",
          new Contact("forsrc@gmail.com", "forsrc", ""),
          "Apache License Version 2.0, January 2004",
          "https://www.apache.org/licenses/LICENSE-2.0");
        return apiInfo;
    }
    
    @Configuration
    public static class WebMvcConfig extends WebMvcConfigurerAdapter {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

            registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
}