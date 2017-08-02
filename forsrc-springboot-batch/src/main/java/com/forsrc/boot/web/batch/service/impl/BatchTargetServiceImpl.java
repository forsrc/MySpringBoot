package com.forsrc.boot.web.batch.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forsrc.boot.batch.pojo.BatchTarget;
import com.forsrc.boot.web.batch.dao.BatchTargetDao;
import com.forsrc.boot.web.batch.service.BatchTargetService;

@Service
public class BatchTargetServiceImpl implements BatchTargetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTargetServiceImpl.class);

    @Autowired
    private BatchTargetDao dao;

    @Override
    public void create() {
        dao.create();
        LOGGER.info("create tabel.");
    }

    @Override
    public void save(BatchTarget bean) {
        dao.save(bean);
        LOGGER.info("Save: {}", bean);
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
        dao.insert(bean);
        LOGGER.info("Insert: {}", bean);
    }
}
