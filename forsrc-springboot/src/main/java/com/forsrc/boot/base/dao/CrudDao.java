package com.forsrc.boot.base.dao;

import java.io.Serializable;

import org.springframework.cache.annotation.CacheConfig;

@CacheConfig(cacheNames = "ehcache_pojo")
public interface CrudDao<T extends Object, ID extends Serializable> // extends
                                                                    // CrudRepository<T,
                                                                    // ID>
{
}
