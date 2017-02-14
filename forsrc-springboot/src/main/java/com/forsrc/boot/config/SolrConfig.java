package com.forsrc.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
public class SolrConfig {

    @Resource
    private Environment env;


}
