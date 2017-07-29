package com.forsrc.boot.batch.rest;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import com.forsrc.boot.batch.pojo.BatchTarget;

@Configuration
@EnableBatchProcessing
public class BatchTargetJonConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTargetJonConfig.class);

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private Environment environment;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public ItemReader<BatchTarget> batchTargetItemReader() {
        return new BatchTargetItemReader(environment.getRequiredProperty("api.batch.target.url"), restTemplate);
    }

    @Bean
    @StepScope
    public ItemProcessor<BatchTarget, BatchTarget> batchTargetItemProcessor() {
        return new BatchTargetItemProcessor();
    }

    @Bean
    @StepScope
    public ItemWriter<BatchTarget> batchTargetItemWriter() {
        return new BatchTargetItemWriter();
    }

    @Bean
    // @formatter:off
    public Step batchTargetStep() {
        return stepBuilderFactory
                .get("batchTargetStep")
                .<BatchTarget, BatchTarget>chunk(1)
                .reader(batchTargetItemReader())
                .processor(batchTargetItemProcessor())
                .writer(batchTargetItemWriter())
                .build();
    }
    // @formatter:on

    @Bean
    public Step stepInit() {
        return stepBuilderFactory.get("stepInit").tasklet(new Tasklet() {
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                LOGGER.info("stepInit execute() --> {}", contribution);
                LOGGER.info("stepInit execute() --> {}", chunkContext);
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step stepDone() {
        return stepBuilderFactory.get("stepDone").tasklet(new Tasklet() {
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                LOGGER.info("stepDone execute() --> {}", contribution);
                LOGGER.info("stepDone execute() --> {}", chunkContext);
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    // @formatter:off
    public Job batchTargetJob() {

        return jobBuilderFactory
                .get("batchTargetJob")
                .incrementer(new RunIdIncrementer())
                .listener(batchTargetJobExecutionListener())
                .flow(stepInit())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(batchTargetStep())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(stepDone())
                .end()
                .build();
    }
    // @formatter:on

    @Bean
    // @formatter:off
    public Job job1() {
        return jobBuilderFactory
                .get("job1")
                .preventRestart()
                .start(stepInit())
                .next(batchTargetStep())
                .next(stepDone())
                .build();
    }
    // @formatter:on

    @Bean
    public JobExecutionListener batchTargetJobExecutionListener() {
        return new JobExecutionListener() {

            @Override
            public void beforeJob(JobExecution jobExecution) {
                LOGGER.info("beforeJob() --> {}", jobExecution);
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                LOGGER.info("afterJob() --> {}", jobExecution);

            }

        };
    }
}
