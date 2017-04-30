package com.forsrc.boot.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SolrConfig {

    @Resource
    private Environment env;

}
