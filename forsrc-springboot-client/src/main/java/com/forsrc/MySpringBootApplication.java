package com.forsrc;

//import de.codecentric.boot.admin.config.EnableAdminServer;

import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableOAuth2Sso
public class MySpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringBootApplication.class, args);
	}

	@Profile("!cloud")
	//@Bean
	RequestDumperFilter requestDumperFilter() {
		return new RequestDumperFilter();
	}
}
