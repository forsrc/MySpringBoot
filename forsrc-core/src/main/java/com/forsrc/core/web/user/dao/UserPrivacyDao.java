package com.forsrc.core.web.user.dao;

import com.forsrc.core.base.dao.BaseDao;
import com.forsrc.pojo.UserPrivacy;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = "ehcache_pojp")
public interface UserPrivacyDao extends BaseDao<UserPrivacy, Long> {
    UserPrivacy findByUsername(String username);
}
