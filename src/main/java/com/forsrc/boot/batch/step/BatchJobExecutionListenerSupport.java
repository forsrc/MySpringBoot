package com.forsrc.boot.batch.step;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BatchJobExecutionListenerSupport extends JobExecutionListenerSupport {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BatchJobExecutionListenerSupport(JdbcTemplate jdbcTemplate) {
        if (this.jdbcTemplate == null) {
            this.jdbcTemplate = jdbcTemplate;
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(jobExecution);
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println(jobExecution.getExitStatus());
        }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);
    }

}
