package com.forsrc.boot.web.user.dao.impl;

import com.forsrc.boot.base.dao.impl.BaseDaoImpl;
import com.forsrc.boot.web.user.dao.UserPrivacyDao;
import com.forsrc.pojo.UserPrivacy;


import org.springframework.stereotype.Repository;

@Repository
public class UserPrivacyDaoImpl extends BaseDaoImpl<UserPrivacy, Long> implements UserPrivacyDao {

    @Override
    public Class<UserPrivacy> getEntityClass() {
        return UserPrivacy.class;
    }
}
