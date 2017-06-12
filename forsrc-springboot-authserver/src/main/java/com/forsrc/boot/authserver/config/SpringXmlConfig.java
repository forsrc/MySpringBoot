package com.forsrc.boot.authserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = { "classpath:applicationContext.xml" })
public class SpringXmlConfig {

}
