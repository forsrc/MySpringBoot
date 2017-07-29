package com.forsrc.boot.batch.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import com.forsrc.boot.batch.pojo.BatchTarget;

@Configuration
public class BatchTargetJonConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTargetJonConfig.class);

    @Bean
    public ItemReader<BatchTarget> batchTargetItemReader(Environment environment, RestTemplate restTemplate) {
        return new BatchTargetItemReader(environment.getRequiredProperty("api.batch.target.url"), restTemplate);
    }

    @Bean
    public ItemProcessor<BatchTarget, BatchTarget> batchTargetItemProcessor() {
        return new BatchTargetItemProcessor();
    }

    @Bean
    public ItemWriter<BatchTarget> batchTargetItemWriter() {
        return new BatchTargetItemWriter();
    }

    @Bean
    public Step batchTargetStep(ItemReader<BatchTarget> batchTargetItemReader,
            ItemProcessor<BatchTarget, BatchTarget> batchTargetProcessor, ItemWriter<BatchTarget> batchTargetItemWriter,
            StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("batchTargetStep").<BatchTarget, BatchTarget>chunk(1).reader(batchTargetItemReader)
                .processor(batchTargetProcessor).writer(batchTargetItemWriter).build();
    }

    @Bean
    public Job batchTargetJob(JobBuilderFactory jobBuilderFactory, @Qualifier("batchTargetStep") Step batchTargetStep) {
        return jobBuilderFactory.get("batchTargetJob").incrementer(new RunIdIncrementer()).flow(batchTargetStep).end()
                .build();
    }
}
