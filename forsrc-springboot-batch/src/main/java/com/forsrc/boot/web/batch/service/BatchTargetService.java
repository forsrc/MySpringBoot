package com.forsrc.boot.web.batch.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.boot.batch.pojo.BatchTarget;

@Service
public interface BatchTargetService {

    public void create();

    public void delete();

    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    public void save(List<BatchTarget> list) throws Exception;

    public int count();

    public void insert(BatchTarget bean);
}
