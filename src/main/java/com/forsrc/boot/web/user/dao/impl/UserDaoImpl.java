package com.forsrc.boot.web.user.dao.impl;

import com.forsrc.boot.base.dao.impl.BaseDaoImpl;
import com.forsrc.boot.web.user.dao.UserDao;

import com.forsrc.pojo.User;
import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public class UserDaoImpl extends BaseDaoImpl<User, Long> implements UserDao {

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public List<User> get(int start, int size) {
        //String hql = entityManager.createStoredProcedureQuery("hql_user_get").toString();
        //System.out.println(hql);
        String hql = "SELECT '******' AS password, u.id, u.username, u.status, u.isAdmin, u.email, u.image, u.createOn, u.updateOn, u.version FROM com.forsrc.pojo.User u WHERE 1 = 1";
        return get(hql, null, start, size);
        //return super.get(start, size);
    }
}
