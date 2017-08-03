package com.forsrc.boot.batch.config;


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

@Configuration
// @EnableBatchProcessing
public class BatchInMemoryConfig {

    @Autowired
    public PlatformTransactionManager transactionManager;

    //@Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public MapJobRepositoryFactoryBean mapJobRepositoryFactory()
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
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        launcher.afterPropertiesSet();
        return launcher;
    }

    @Bean
    public JobExplorer jobExplorer(MapJobRepositoryFactoryBean mapJobRepositoryFactory) {
        return new SimpleJobExplorer(mapJobRepositoryFactory.getJobInstanceDao(),
                mapJobRepositoryFactory.getJobExecutionDao(), mapJobRepositoryFactory.getStepExecutionDao(),
                mapJobRepositoryFactory.getExecutionContextDao());
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
