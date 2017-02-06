/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.forsrc.boot.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = "ehcache_pojp")
public interface BaseDao<E, PK extends Serializable> {

    public static final int SIZE_MAX = 100;

    @CachePut
    public void save(E e);

    @CacheEvict()
    public void delete(E e);

    @Cacheable(condition = "#result != null")
    public E get(PK pk);

    @Cacheable(condition = "#result != null")
    public List<E> get(int start, int size);

    @Cacheable(condition = "#result != null")
    public <T> List<T> get(Class<T> cls, int start, int size);

    @Cacheable(condition = "#result != null")
    public List<E> get(String hql, Map<String, Object> params, int start, int size);

    @CachePut(condition = "#result != null")
    public void update(E e);

    @Cacheable(condition = "#result != null")
    public <T> List<T> createNamedQuery(String namedQuery, Map<String, Object> params, int start, int size);

    @Cacheable(condition = "#result != null")
    public <T> List<T> createNamedQuery(String namedQuery, Class<T> cls, Map<String, Object> params, int start, int size);

    public int executeUpdateNamedQuery(String namedQuery, Map<String, Object> params);

    public int executeUpdate(String hql, Map<String, Object> params);

    @Cacheable
    public long count();

    @Cacheable
    public <T> long count(Class<T> cls);

    public Class<E> getEntityClass();
}
