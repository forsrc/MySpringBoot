package com.forsrc.boot.web.batch.service.impl;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forsrc.boot.batch.pojo.BatchTarget;
import com.forsrc.boot.web.batch.dao.BatchTargetDao;
import com.forsrc.boot.web.batch.service.BatchTargetService;

@Service
public class BatchTargetServiceImpl implements BatchTargetService {

    @Autowired
    private BatchTargetDao dao;

    @Override
    public void create() {
        dao.create();
    }

    @Override
    public void save(BatchTarget bean) {
        dao.save(bean);
    }

}
