package com.forsrc.boot.batch.config;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "userEntityManager", transactionManagerRef = "userTransactionManager", basePackages = {
        "com.forsrc..dao" })
public class AtomikosJpaConfig {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Value("${db.packages:com.forsrc}")
    private String packages;

    @Value("${db.primaryPersistenceUnit:primaryPersistenceUnit}")
    private String persistenceUnit;

    @Bean(name = { "dataSource", "userDataSource", "primaryDataSource" }, initMethod = "init", destroyMethod = "close")
    @Primary
    public DataSource userDataSource() {
        JdbcDataSource h2XaDataSource = new JdbcDataSource();
        h2XaDataSource.setURL(url);
        h2XaDataSource.setUser(username);
        h2XaDataSource.setPassword(password);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(h2XaDataSource);
        xaDataSource.setMinPoolSize(1);
        xaDataSource.setMaxPoolSize(5);
        xaDataSource.setBorrowConnectionTimeout(60);
        xaDataSource.setUniqueResourceName("xads");
        return xaDataSource;
    }

    @Bean(name = "userTransaction")
    public UserTransaction userTransaction() throws Throwable {
        UserTransactionImp userTransaction = new UserTransactionImp();
        userTransaction.setTransactionTimeout(10000);
        AtomikosJtaPlatform.transaction = userTransaction;
        return userTransaction;
    }

    @Bean(name = "userTransactionManager", initMethod = "init", destroyMethod = "close")
    public TransactionManager userTransactionManager() throws Throwable {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);

        AtomikosJtaPlatform.transactionManager = userTransactionManager;

        return userTransactionManager;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        hibernateJpaVendorAdapter.setDatabase(Database.H2);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() throws Throwable {

        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        properties.put("spring.jpa.hibernate.naming.physical-strategy",
                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(userDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter());
        entityManager.setPackagesToScan(packages);
        entityManager.setPersistenceUnitName(persistenceUnit);
        entityManager.setJpaPropertyMap(properties);

        return entityManager;
    }

    @Bean(name = { "entityManager", "userEntityManager", "entityManagerPrimary" })
    @DependsOn("userTransactionManager")
    @Primary
    public EntityManager userEntityManager() throws Throwable {

        return localContainerEntityManagerFactoryBean().getObject().createEntityManager();
    }

    @Bean(name = { "transactionManager", "transactionManagerPrimary" })
    @DependsOn({ "userTransaction", "userTransactionManager" })
    @Primary
    public PlatformTransactionManager jtaTransactionManager() throws Throwable {

        return new JtaTransactionManager(AtomikosJtaPlatform.transaction, AtomikosJtaPlatform.transactionManager);
    }

    public static class AtomikosJtaPlatform extends AbstractJtaPlatform {

        private static final long serialVersionUID = 1L;

        public static TransactionManager transactionManager;
        public static UserTransaction transaction;

        @Override
        protected TransactionManager locateTransactionManager() {
            return transactionManager;
        }

        @Override
        protected UserTransaction locateUserTransaction() {
            return transaction;
        }

    }
}
