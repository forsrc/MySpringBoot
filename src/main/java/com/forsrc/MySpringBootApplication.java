package com.forsrc;

//import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
//@EnableAdminServer
@ComponentScan
@EnableAutoConfiguration
@EnableJms
@EnableWebSocket
public class MySpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySpringBootApplication.class, args);
	}
}
