package com.forsrc.boot.base.dao;

import java.io.Serializable;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.repository.CrudRepository;

@CacheConfig(cacheNames = "ehcache_pojp")
public interface CrudDao<T extends Object, ID extends Serializable> //extends CrudRepository<T, ID>
{
}
