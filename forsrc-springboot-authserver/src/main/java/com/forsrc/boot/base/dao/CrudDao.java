package com.forsrc.boot.base.dao;

import org.springframework.cache.annotation.CacheConfig;

import java.io.Serializable;

@CacheConfig(cacheNames = "ehcache_pojp")
public interface CrudDao<T extends Object, ID extends Serializable> //extends CrudRepository<T, ID>
{
}
