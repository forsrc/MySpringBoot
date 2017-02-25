package com.forsrc.boot.adminserver.web.init.dao.impl;

import com.forsrc.boot.adminserver.web.init.dao.InitDao;
import com.forsrc.core.base.dao.impl.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

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
