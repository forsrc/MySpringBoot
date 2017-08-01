package com.forsrc.boot.batch.rest;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.forsrc.boot.batch.pojo.BatchTarget;
import com.forsrc.boot.web.batch.service.BatchTargetService;

public class BatchTargetItemWriter implements ItemWriter<BatchTarget> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTargetItemWriter.class);

    @Autowired
    private BatchTargetService service;
    @Override
    public void write(List<? extends BatchTarget> items) throws Exception {
        // @formatter:off
        items.stream().forEach(item -> {
            service.save(item);
            LOGGER.info("BatchTargetItemWriter: {} -> {}", items.size(), items);
        });
     // @formatter:on
    }

    @PostConstruct
    public void init() {
        service.create();
    }
}
