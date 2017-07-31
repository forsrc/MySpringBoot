package com.forsrc;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.admin.annotation.EnableBatchAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = { HypermediaAutoConfiguration.class, MultipartAutoConfiguration.class })
@ComponentScan(basePackages = "com.forsrc")
@EnableAutoConfiguration

@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableBatchProcessing
//@EnableBatchAdmin
@EnableScheduling

public class MySpringBootApplication {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {

        SpringApplication.run(MySpringBootApplication.class, args);
    }

}
