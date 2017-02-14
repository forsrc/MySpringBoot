package com.forsrc.boot.batch.step;


import com.forsrc.boot.batch.user.UserItemProcessor;
import com.forsrc.pojo.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class Batch001 {

    @Bean // 1
    public ItemReader<User> reader() {
        FlatFileItemReader<User> reader = new FlatFileItemReader<User>();

        reader.setResource(new ClassPathResource("data/csv/user.csv"));
        reader.setLineMapper(new DefaultLineMapper<User>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"username", "email"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
                setTargetType(User.class);
            }});
        }});
        return reader;
    }

    @Bean // 2
    public UserItemProcessor processor() {
        return new UserItemProcessor();
    }

    @Bean // 3
    public ItemWriter<User> writer(DataSource dataSource) {
//        JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<User>();
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
//        writer.setSql("");
//        writer.setDataSource(dataSource);
        ItemWriter<User> writer = new ItemWriter<User>() {
            @Override
            public void write(List<? extends User> list) throws Exception {
                for (User u : list) {
                    System.out.println(u);
                }
            }
        };
        return writer;
    }

    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step batch001, JobExecutionListener listener) {
        return jobs.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(batch001)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<User> reader,
                      ItemWriter<User> writer, ItemProcessor<User, User> processor) {
        return stepBuilderFactory.get("batch001")
                .<User, User>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
