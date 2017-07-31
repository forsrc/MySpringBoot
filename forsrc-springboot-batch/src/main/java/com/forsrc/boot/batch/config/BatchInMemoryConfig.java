package com.forsrc.boot.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.admin.annotation.EnableBatchAdmin;
import org.springframework.batch.admin.service.JobService;
import org.springframework.batch.admin.service.SimpleJobServiceFactoryBean;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

// @Configuration
// @EnableBatchAdmin
public class BatchInMemoryConfig {

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public MapJobRepositoryFactoryBean mapJobRepositoryFactory(PlatformTransactionManager transactionManager)
            throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(transactionManager);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public JobRepository jobRepository(MapJobRepositoryFactoryBean repositoryFactory) throws Exception {
        return repositoryFactory.getObject();
    }

    @Bean
    public JobExplorer jobExplorer(MapJobRepositoryFactoryBean mapJobRepositoryFactory) {
        return new SimpleJobExplorer(mapJobRepositoryFactory.getJobInstanceDao(),
                mapJobRepositoryFactory.getJobExecutionDao(), mapJobRepositoryFactory.getStepExecutionDao(),
                mapJobRepositoryFactory.getExecutionContextDao());
    }
    

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Bean
    public JobService jobService(JobRepository jobRepository, SimpleJobLauncher jobLauncher,
            PlatformTransactionManager transactionManager) throws Exception {
        SimpleJobServiceFactoryBean factory = new SimpleJobServiceFactoryBean();
        factory.setTransactionManager(transactionManager);
        factory.setJobRepository(jobRepository);
        factory.setJobLauncher(jobLauncher);
        return factory.getObject();
    }

    @Bean
    public StepBuilderFactory stepBuilderFactory(JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {
        return new StepBuilderFactory(jobRepository, transactionManager);
    }

    @Bean
    public JobBuilderFactory jobBuilderFactory(JobRepository jobRepository) {
        return new JobBuilderFactory(jobRepository);
    }
}
