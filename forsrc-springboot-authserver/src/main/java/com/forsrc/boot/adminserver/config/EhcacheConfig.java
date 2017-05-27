package com.forsrc.boot.adminserver.config;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import net.sf.ehcache.Ehcache;

@Configuration
@EnableCaching
public class EhcacheConfig extends CachingConfigurerSupport {

    @Autowired
    private EhCacheCacheManager ehCacheCacheManager;

    @Bean
    public org.springframework.cache.Cache cache() {
        return ehCacheCacheManager.getCache("ehcache_pojo");
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
        Ehcache ehcache = ehCacheCacheManager.getCacheManager().getEhcache("ehcache_pojo");
        return ehcache;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        // return new SimpleKeyGenerator();
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                System.out.println("---->" + sb.toString());
                return sb.toString();
            }
        };
    }
}
