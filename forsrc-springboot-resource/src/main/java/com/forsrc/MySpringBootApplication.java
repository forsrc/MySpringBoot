package com.forsrc;

import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class MySpringBootApplication {

    static {
        //for localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

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
            System.setProperty("javax.net.ssl.trustStore", new ClassPathResource("/truststore.keystore").getFile().getAbsolutePath());
            System.setProperty("javax.net.ssl.trustStorePassword", "apache");
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
