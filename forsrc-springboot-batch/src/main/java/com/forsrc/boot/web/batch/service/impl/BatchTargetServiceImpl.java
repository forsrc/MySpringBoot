package com.forsrc.boot.web.batch.service.impl;

import java.util.List;

import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.boot.batch.pojo.BatchTarget;
import com.forsrc.boot.web.batch.dao.BatchTargetDao;
import com.forsrc.boot.web.batch.service.BatchTargetService;

@Service
public class BatchTargetServiceImpl implements BatchTargetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTargetServiceImpl.class);

    @Autowired
    private BatchTargetDao dao;

    @Autowired
    private UserTransaction userTransaction;

    @Override
    public void create() {
        dao.create();
        LOGGER.info("create tabel.");
    }

    @Override
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void save(List<BatchTarget> list) throws Exception {
        userTransaction.begin();
        try {
            for (BatchTarget bean : list) {
                LOGGER.info("To save: {}", bean);
                count();
                dao.save(bean);
                count();
                LOGGER.info("Saved: {}", bean);
            }
            userTransaction.commit();
        } catch (Exception e) {
            userTransaction.rollback();
        }
    }

    @Override
    public void delete() {
        dao.delete();
        LOGGER.info("Delete all.");
    }

    public int count() {
        int count = dao.count();
        LOGGER.info("Count: {}", count);
        return count;
    }

    @Override
    public void insert(BatchTarget bean) {
        count();
        dao.insert(bean);
        LOGGER.info("Insert: {}", bean);
        count();
    }
}
