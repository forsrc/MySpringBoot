package com.forsrc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.forsrc")
@EnableAutoConfiguration
@EnableConfigServer
public class MySpringBootApplication {

    public static void main(String[] args) {

        SpringApplication.run(MySpringBootApplication.class, args);
    }

}
