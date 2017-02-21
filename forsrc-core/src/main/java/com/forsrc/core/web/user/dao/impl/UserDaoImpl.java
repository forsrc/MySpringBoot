package com.forsrc.core.web.user.dao.impl;

import com.forsrc.core.base.dao.impl.BaseDaoImpl;
import com.forsrc.core.web.user.dao.UserDao;
import com.forsrc.pojo.User;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Long> implements UserDao {

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public List<User> get(int start, int size) {
        //Query query = entityManager.createNamedQuery("hql_user_get");
        //query.setFirstResult(start);
        //query.setMaxResults(size);
        //String hql = "SELECT '******' AS password, u.id, u.username, u.status, u.isAdmin, u.email, u.image, u.createOn, u.updateOn, u.version FROM com.forsrc.pojo.User u WHERE 1 = 1";
        //return (List<User>)query.getResultList();
        //return super.get(start, size);
        return createNamedQuery("hql_user_get", null, start, size);
    }

    @Override
    public List<User> findByUsername(String username) {
        Query query = entityManager.createNamedQuery("sql_user_findByUserId", getEntityClass());
        query.setParameter("username", username);
        return query.getResultList();
    }
}
