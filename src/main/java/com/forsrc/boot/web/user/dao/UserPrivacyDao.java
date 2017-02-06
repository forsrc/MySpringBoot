package com.forsrc.boot.web.user.dao;

import com.forsrc.boot.base.dao.BaseDao;
import com.forsrc.pojo.UserPrivacy;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = "ehcache_pojp")
public interface UserPrivacyDao extends BaseDao<UserPrivacy, Long> {
    public UserPrivacy findByUsername(String username);
}
