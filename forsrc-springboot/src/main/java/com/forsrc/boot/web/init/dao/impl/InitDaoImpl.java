package com.forsrc.boot.web.init.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.forsrc.boot.base.dao.impl.BaseDaoImpl;
import com.forsrc.boot.web.init.dao.InitDao;

@Repository
public class InitDaoImpl extends BaseDaoImpl<Object, Long> implements InitDao {

    @Override
    public Class<Object> getEntityClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initDb() throws Exception {
        Query query = null;
        query = entityManager.createNamedQuery("sql_user_init");
        query.executeUpdate();
        query = entityManager.createNamedQuery("sql_user_privacy_init");
        query.executeUpdate();
        query = entityManager.createNamedQuery("sql_role_init");
        query.executeUpdate();
        query = entityManager.createNamedQuery("sql_user_role_init");
        query.executeUpdate();
    }
}
