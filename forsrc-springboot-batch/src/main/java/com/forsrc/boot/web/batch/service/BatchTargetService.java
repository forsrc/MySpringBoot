package com.forsrc.boot.web.batch.service;

import org.springframework.stereotype.Service;

import com.forsrc.boot.batch.pojo.BatchTarget;

@Service
public interface BatchTargetService {

    public void create();

    public void delete();

    public void save(BatchTarget bean);

    public int count();

    public void insert(BatchTarget bean);
}
