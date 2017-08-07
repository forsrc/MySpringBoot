package com.forsrc.boot.batch.rest;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.forsrc.boot.batch.pojo.BatchTarget;
import com.forsrc.boot.web.batch.service.BatchTargetService;


public class BatchTargetItemWriter implements ItemWriter<BatchTarget> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTargetItemWriter.class);

    @Autowired
    private BatchTargetService service;

    @Override
    //@Transactional(transactionManager = "transactionManagerPrimary", propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    public void write(List<? extends BatchTarget> items) throws Exception {
        // @formatter:off
        LOGGER.info("BatchTargetItemWriter: size --> {} -> {}", items.size(), items);
        for (BatchTarget item : items) {
            if (item.getId().equals(0L)) {
                LOGGER.info("Continue: {}", item);
                continue;
            }
            if (item.getId().equals(18L)) {
                throw new RuntimeException("Test");
            }
            service.insert(item);
        }
     // @formatter:on
    }

    @PostConstruct
    public void init() {
        // service.create();
    }

    @PreDestroy
    public void preDestroy() {
        service.count();
    }
}
