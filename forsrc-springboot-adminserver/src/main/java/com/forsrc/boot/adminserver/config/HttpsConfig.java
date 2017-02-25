package com.forsrc.adminserver.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

//@Configuration
public class HttpsConfig {

    @Resource
    private Environment env;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    @Bean
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        int port = Integer.parseInt(env.getProperty("server.port"));
        int httpPort = env.getProperty("server.http.port") == null ? port + 1000
                : Integer.parseInt(env.getProperty("server.http.port"));
        connector.setPort(httpPort);
        connector.setSecure(false);
        connector.setRedirectPort(port);
        return connector;
    }

    //@Bean
    public Connector httpsConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("https");
        int port = Integer.parseInt(env.getProperty("server.https.port"));
        connector.setPort(port);
        connector.setSecure(true);
        //connector.setRedirectPort(port);
        try {
            File keystore = new ClassPathResource("server.jks").getFile();
            File truststore = new ClassPathResource("client.jks").getFile();
            Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
            protocol.setSSLEnabled(true);
            protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass("apache");
            protocol.setTruststoreFile(truststore.getAbsolutePath());
            protocol.setTruststorePass("apache");
            protocol.setKeyAlias("apache");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connector;
    }

}
