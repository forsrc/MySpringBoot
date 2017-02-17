package com.forsrc;

//import de.codecentric.boot.admin.config.EnableAdminServer;

import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.io.IOException;

@EnableResourceServer
@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class MySpringBootApplication {

	static {
		//for localhost testing only
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
				new javax.net.ssl.HostnameVerifier(){

					public boolean verify(String hostname,
										  javax.net.ssl.SSLSession sslSession) {
						if (hostname.equals("localhost")) {
							return true;
						}
						return false;
					}
				});
	}

	public static void main(String[] args) {
		try {
			System.setProperty("javax.net.ssl.trustStore", new ClassPathResource("/client.jks").getFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		SpringApplication.run(MySpringBootApplication.class, args);
	}

	@Profile("!cloud")
	//@Bean
	RequestDumperFilter requestDumperFilter() {
		return new RequestDumperFilter();
	}
}
