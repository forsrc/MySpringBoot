package com.forsrc.boot.batch.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.forsrc.boot.batch.pojo.BatchTarget;

public class BatchTargetItemWriter implements ItemWriter<BatchTarget> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTargetItemWriter.class);

    @Override
    public void write(List<? extends BatchTarget> items) throws Exception {
        items.stream().forEach(item -> LOGGER.info("BatchTargetItemWriter: {}", item));

    }

}
