package com.forsrc.core.base.dao;

import java.io.Serializable;
import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = "ehcache_pojp")
public interface CrudDao<T extends Object, ID extends Serializable> //extends CrudRepository<T, ID>
{
}
