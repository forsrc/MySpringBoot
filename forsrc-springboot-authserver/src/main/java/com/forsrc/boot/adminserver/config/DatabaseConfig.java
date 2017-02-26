package com.forsrc.boot.adminserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Autowired
    private Environment env;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    //@Bean(name = "dataSource")
    //@Primary
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource);

        entityManagerFactory.setPackagesToScan(
                env.getProperty("entitymanager.packagesToScan"));

        String persistenceXmlLocation = env.getProperty("entitymanager.persistenceXmlLocation");
        if (persistenceXmlLocation != null) {
            entityManagerFactory.setPersistenceXmlLocation(persistenceXmlLocation);
        }

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.put(
                "hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        properties.put(
                "hibernate.show_sql",
                env.getProperty("hibernate.show_sql"));
        properties.put(
                "hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));

        properties.put(
                "hibernate.format_sql",
                env.getProperty("hibernate.format_sql"));
        String mappingResources = env.getProperty("hibernate.mappingResources");
        if (mappingResources != null) {
            //entityManagerFactory.setMappingResources(DatabaseConfig.class.getResource(mappingResources).toString());
        }
        entityManagerFactory.setJpaProperties(properties);
        return entityManagerFactory;
    }

    /*
     @Bean
     public LocalSessionFactoryBean sessionFactory() throws IOException {
     LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
     String mappingResources = env.getProperty("hibernate.mappingResources");
     Properties properties = new Properties();
     properties.put(
     "hibernate.dialect",
     env.getProperty("hibernate.dialect"));
     properties.put(
     "hibernate.show_sql",
     env.getProperty("hibernate.show_sql"));
     properties.put(
     "hibernate.hbm2ddl.auto",
     env.getProperty("hibernate.hbm2ddl.auto"));

     properties.put(
     "hibernate.format_sql",
     env.getProperty("hibernate.format_sql"));
     if (mappingResources != null) {
     
     }

     sessionFactoryBean.setHibernateProperties(properties);
     return sessionFactoryBean;
     }
     */
    /*
     @Bean(name = "jpaTransactionManager")
     //@Qualifier("jpaTransactionManager")
     public JpaTransactionManager jpaTransactionManager() {
     JpaTransactionManager transactionManager =
     new JpaTransactionManager();
     transactionManager.setEntityManagerFactory(
     entityManagerFactory.getObject());
     return transactionManager;
     }

     @Bean(name = "txManager02")
     //@Qualifier(value = "txManager02")
     public PlatformTransactionManager txManager02() {
     return new DataSourceTransactionManager(dataSource);
     }
     */
    @Bean(name = "transactionManager")
    //@Primary
    public PlatformTransactionManager txManager01() {
        JpaTransactionManager transactionManager =
                new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                entityManagerFactory.getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    //@Bean(name = "jdbcTemplate")
    //@Primary
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
