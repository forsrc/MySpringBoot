package com.forsrc.boot.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.net.MalformedURLException;

@Configuration
public class SolrConfig {

    @Resource
    private Environment env;

    @Bean
    public SolrClient solrClient() throws MalformedURLException, IllegalStateException {
        return new HttpSolrClient(env.getRequiredProperty("solr.host"));
    }


}
