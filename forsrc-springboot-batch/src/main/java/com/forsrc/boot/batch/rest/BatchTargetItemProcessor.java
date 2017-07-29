package com.forsrc.boot.batch.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.forsrc.boot.batch.pojo.BatchTarget;

public class BatchTargetItemProcessor implements ItemProcessor<BatchTarget, BatchTarget> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTargetItemProcessor.class);

    @Override
    public BatchTarget process(BatchTarget item) throws Exception {
        LOGGER.info("BatchTargetItemProcessor: {}", item);
        return item;
    }

}
