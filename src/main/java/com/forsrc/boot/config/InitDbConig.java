package com.forsrc.boot.config;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitDbConig {

    @Bean
    public InitDbConig init(@Autowired EntityManager entityManager) {
        System.out.println("--> entityManager :" + entityManager);
        Query query = null;
        try {
            query = entityManager.createNamedQuery("sql_user_init");
            query.executeUpdate();
            entityManager.flush();
            query = entityManager.createNamedQuery("sql_user_privacy_init");
            query.executeUpdate();
            entityManager.flush();
            query = entityManager.createNamedQuery("sql_role_init");
            query.executeUpdate();
            entityManager.flush();
            query = entityManager.createNamedQuery("sql_user_role_init");
            query.executeUpdate();
            entityManager.flush();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return this;
    }
}
