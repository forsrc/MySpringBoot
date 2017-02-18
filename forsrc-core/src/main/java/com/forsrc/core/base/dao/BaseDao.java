package com.forsrc.core.base.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Repository
@CacheConfig(cacheNames = "ehcache_pojp")
public interface BaseDao<E, PK extends Serializable> {

    int SIZE_MAX = 100;

    @CachePut
    void save(E e);

    @CacheEvict()
    void delete(E e);

    @Cacheable(condition = "#result != null")
    E get(PK pk);

    @Cacheable(condition = "#result != null")
    List<E> get(int start, int size);

    @Cacheable(condition = "#result != null")
    <T> List<T> get(Class<T> cls, int start, int size);

    @Cacheable(condition = "#result != null")
    List<E> get(String hql, Map<String, Object> params, int start, int size);

    @CachePut(condition = "#result != null")
    void update(E e);

    @Cacheable(condition = "#result != null")
    <T> List<T> createNamedQuery(String namedQuery, Map<String, Object> params, int start, int size);

    @Cacheable(condition = "#result != null")
    <T> List<T> createNamedQuery(String namedQuery, Class<T> cls, Map<String, Object> params, int start, int size);

    int executeUpdateNamedQuery(String namedQuery, Map<String, Object> params);

    int executeUpdate(String hql, Map<String, Object> params);

    @Cacheable
    long count();

    @Cacheable
    <T> long count(Class<T> cls);

    Class<E> getEntityClass();
}
