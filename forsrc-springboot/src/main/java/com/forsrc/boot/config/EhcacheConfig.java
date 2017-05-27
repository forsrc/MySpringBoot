package com.forsrc.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import net.sf.ehcache.Ehcache;

@Configuration
@EnableCaching
public class EhcacheConfig {

    @Autowired
    private EhCacheCacheManager ehCacheCacheManager;

    @Bean
    public org.springframework.cache.Cache cache() {
        return ehCacheCacheManager.getCache("ehcache_pojo_aop");
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean) {
        return new EhCacheCacheManager(bean.getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;

    }

    @Bean
    public Ehcache ehcache(EhCacheCacheManager ehCacheCacheManager) {
        Ehcache ehcache = ehCacheCacheManager.getCacheManager().getEhcache("ehcache_pojo_aop");
        return ehcache;
    }
}
