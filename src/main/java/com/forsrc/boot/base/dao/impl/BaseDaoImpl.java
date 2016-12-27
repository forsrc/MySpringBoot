package com.forsrc.boot.base.dao.impl;

import com.forsrc.boot.base.dao.BaseDao;
import static com.forsrc.boot.base.dao.BaseDao.SIZE_MAX;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseDaoImpl<E, PK extends Serializable> implements BaseDao<E, PK> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public void save(E e) {
        entityManager.persist(e);
    }

    @Override
    public void delete(E e) {
        if (entityManager.contains(e)) {
            entityManager.remove(e);
            return;
        }
        entityManager.remove(entityManager.merge(e));
    }

    @Override
    public E get(PK pk) {
        return entityManager.find(getEntityClass(), pk);
    }

    @Override
    public List<E> get(int start, int size) {
        return get(MessageFormat.format("FROM {0}", getEntityClassName()), null, start, size);
    }

    @Override
    public <T> List<T> get(Class<T> cls, int start, int size) {

        return (List<T>) get(MessageFormat.format("FROM {0}", cls.getName()), null, start, size);
    }

    @Override
    public List<E> get(String hql, Map<String, Object> params, int start, int size) {
        Query query = entityManager.createQuery(hql)
                .setFirstResult(start < 0 ? 0 : start)
                .setMaxResults(size > SIZE_MAX ? SIZE_MAX : size);
        if (params == null) {
            return query.getResultList();
        }
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            query.setParameter(entry.getKey(), entry.getKey());
        }

        return query.getResultList();
    }

    @Override
    public void update(E e) {
        entityManager.merge(e);
    }

    @Override
    public <T> List<T> createNamedQuery(String namedQuery, Map<String, Object> params, int start, int size) {
        return createNamedQuery(namedQuery, null, params, start, size);
    }

    @Override
    public <T> List<T> createNamedQuery(String namedQuery, Class<T> cls, Map<String, Object> params, int start, int size) {
        Query query = (cls != null ? entityManager.createNamedQuery(namedQuery, cls) : entityManager.createNamedQuery(namedQuery))
                .setFirstResult(start < 0 ? 0 : start)
                .setMaxResults(size > SIZE_MAX ? SIZE_MAX : size);

        if (params == null) {
            return query.getResultList();
        }
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            query.setParameter(entry.getKey(), entry.getKey());
        }

        return query.getResultList();
    }

    @Override
    public int executeUpdateNamedQuery(String namedQuery, Map<String, Object> params) {
        Query query = entityManager.createNamedQuery(namedQuery);
        if (params == null) {
            return query.executeUpdate();
        }
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            query.setParameter(entry.getKey(), entry.getKey());
        }
        return query.executeUpdate();
    }

    @Override
    public int executeUpdate(String hql, Map<String, Object> params) {
        Query query = entityManager.createQuery(hql);
        if (params == null) {
            return query.executeUpdate();
        }
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            query.setParameter(entry.getKey(), entry.getKey());
        }
        return query.executeUpdate();
    }

    @Override
    public long count() {
        return count(getEntityClass());
    }

    @Override
    public <T> long count(Class<T> cls) {
        String hql = MessageFormat.format("SELECT COUNT(1) FROM {0}", cls.getName());
        Object count = (Long) entityManager.createQuery(hql).setFirstResult(0).setMaxResults(1).getSingleResult();
        if (count instanceof Long) {
            return ((Long) count).longValue();
        }
        if (count instanceof Integer) {
            return ((Integer) count).longValue();
        }
        return 0L;
    }

    public abstract Class<E> getEntityClass();

    protected String getEntityClassName() {
        Class<E> cls = getEntityClass();
        if (cls == null) {
            throw new UnsupportedOperationException("BaseDaoImpl.getEntityClass() not implemented yet.");
        }
        return cls.getName();
    }
}
