package com.forsrc.boot.batch.rest;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchTargetJobLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchTargetJobLauncher.class);

    @Autowired
    @Qualifier("batchTargetJob")
    //@Qualifier("job1")
    private Job job;

    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Scheduled(cron = "${api.batch.target.cron}")
    public void doMain() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
            JobInstanceAlreadyCompleteException {
        LOGGER.info("----> START");
        JobExecution jobExecution = jobLauncher.run(job, batchTargetJobParameters());
        LOGGER.info("{}", jobExecution);
        LOGGER.info("----> END");
    }

    private JobParameters batchTargetJobParameters() {

        // @formatter:off
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("now", new Date())
                .toJobParameters();
        // @formatter:on

        return jobParameters;
    }
}
