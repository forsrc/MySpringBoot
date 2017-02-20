package com.forsrc.core.web.user.dao;

import com.forsrc.core.base.dao.BaseDao;
import com.forsrc.pojo.User;
import java.util.List;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = "ehcache_pojp")
public interface UserDao extends BaseDao<User, Long> {

    List<User> findByUsername(String username);
}
