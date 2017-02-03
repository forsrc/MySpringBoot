package com.forsrc.boot.web.user.dao.impl;

import com.forsrc.boot.base.dao.impl.BaseDaoImpl;
import com.forsrc.boot.web.user.dao.UserPrivacyDao;
import com.forsrc.pojo.UserPrivacy;


import org.springframework.stereotype.Repository;

import javax.persistence.Query;

@Repository
public class UserPrivacyDaoImpl extends BaseDaoImpl<UserPrivacy, Long> implements UserPrivacyDao {

    @Override
    public Class<UserPrivacy> getEntityClass() {
        return UserPrivacy.class;
    }

    @Override
    public UserPrivacy findByUsername(String username) {
        Query query = entityManager.createNativeQuery("sql_user_privacy_findByUsername", UserPrivacy.class);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return (UserPrivacy) query.getSingleResult();
    }
}
