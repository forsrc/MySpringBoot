package com.forsrc.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Configuration
public class InitDbConig {

    @Component
    class InitDb {


        @Autowired
        public EntityManager entityManager;

        public InitDb() {
            init();
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void init() {

            Query query = null;
            try {
                query = entityManager.createNamedQuery("sql_user_init");
                query.executeUpdate();
                query = entityManager.createNamedQuery("sql_user_privacy_init");
                query.executeUpdate();
                query = entityManager.createNamedQuery("sql_role_init");
                query.executeUpdate();
                query = entityManager.createNamedQuery("sql_user_role_init");
                query.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
