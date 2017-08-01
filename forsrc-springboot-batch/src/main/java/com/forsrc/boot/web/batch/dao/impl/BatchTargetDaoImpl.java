package com.forsrc.boot.web.batch.dao.impl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.forsrc.boot.batch.pojo.BatchTarget;
import com.forsrc.boot.web.batch.dao.BatchTargetDao;
import com.forsrc.boot.web.batch.dao.mapper.BatchTargetMapper;

@Repository
public class BatchTargetDaoImpl implements BatchTargetDao {

    @Autowired
    private BatchTargetMapper mapper;

    @Autowired
    @Qualifier("entityManagerPrimary")
    private EntityManager entityManager;
    
    @Override
    public void create() {
        mapper.create();
    }

    @Override
    public void save(BatchTarget bean) {
        entityManager.persist(bean);
    }
}
