package com.forsrc.core.web.user.dao;

import com.forsrc.core.base.dao.BaseDao;
import com.forsrc.pojo.UserRole;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "ehcache_pojp")
public interface UserRoleDao extends BaseDao<UserRole, Long> {

    List<UserRole> findByUserId(Long userId);
}
