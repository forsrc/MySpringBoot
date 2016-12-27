/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.forsrc.boot.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDao<E, PK extends Serializable> {

    public static final int SIZE_MAX = 100;

    public void save(E e);

    public void delete(E e);

    public E get(PK pk);

    public List<E> get(int start, int size);

    public <T> List<T> get(Class<T> cls, int start, int size);

    public List<E> get(String hql, Map<String, Object> params, int start, int size);

    public void update(E e);

    public <T> List<T> createNamedQuery(String namedQuery, Map<String, Object> params, int start, int size);

    public <T> List<T> createNamedQuery(String namedQuery, Class<T> cls, Map<String, Object> params, int start, int size);

    public Class<E> getEntityClass();
}
