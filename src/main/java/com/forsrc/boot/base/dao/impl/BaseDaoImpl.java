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
import javax.transaction.Transactional;

@Transactional
public abstract class BaseDaoImpl<E, PK extends Serializable> implements BaseDao<E, PK> {

    @PersistenceContext
    private EntityManager entityManager;

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
    public E get(long id) {
        return entityManager.find(getEntityClass(), id);
    }

    @Override
    public List<E> get(int start, int size) {
        return get(MessageFormat.format("from {0}", getEntityClassName()), null, start, size);
    }

    @Override
    public <T> List<T> get(Class<T> cls, int start, int size) {

        return (List<T>) get(MessageFormat.format("from {0}", cls.getName()), null, start, size);
    }

    @Override
    public List<E> get(String hql, Map<String, Object> params, int start, int size) {
        Query query = entityManager.createQuery(hql)
                .setFirstResult(start)
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

    public abstract Class<E> getEntityClass();

    protected String getEntityClassName() {
        Class<E> cls = getEntityClass();
        if (cls == null) {
            throw new UnsupportedOperationException("BaseDaoImpl.getEntityClass() not implemented yet.");
        }
        return cls.getName();
    }
}
