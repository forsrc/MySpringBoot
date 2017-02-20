package com.forsrc.core.web.user.dao;

import com.forsrc.core.base.dao.BaseDao;
import com.forsrc.pojo.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

@Repository
@CacheConfig(cacheNames = "ehcache_pojp")
public interface RoleDao extends BaseDao<Role, Long> {

    @Cacheable
    List<Role> getRoles();
}
