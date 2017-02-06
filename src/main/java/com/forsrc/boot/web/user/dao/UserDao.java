package com.forsrc.boot.web.user.dao;

import com.forsrc.boot.base.dao.BaseDao;
import com.forsrc.pojo.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = "ehcache_pojp")
public interface UserDao extends BaseDao<User, Long> {

}
