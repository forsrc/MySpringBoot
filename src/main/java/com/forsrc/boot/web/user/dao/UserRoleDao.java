package com.forsrc.boot.web.user.dao;

import com.forsrc.boot.base.dao.BaseDao;
import com.forsrc.pojo.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.cache.annotation.CacheConfig;

@Repository
@CacheConfig(cacheNames = "ehcache_pojp")
public interface UserRoleDao extends BaseDao<UserRole, Long> {

    public List<UserRole> findByUserId(Long userId);
}
