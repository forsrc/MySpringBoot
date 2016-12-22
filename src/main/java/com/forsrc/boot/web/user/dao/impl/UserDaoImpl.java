package com.forsrc.boot.web.user.dao.impl;

import com.forsrc.boot.base.dao.impl.BaseDaoImpl;
import com.forsrc.boot.web.user.dao.UserDao;

import com.forsrc.pojo.User;

import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserDaoImpl extends BaseDaoImpl<User, Long> implements UserDao {

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}
