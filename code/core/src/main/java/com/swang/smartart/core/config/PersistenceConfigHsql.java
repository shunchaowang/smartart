package com.swang.smartart.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Here we have configured DataSource and JPA EntityManagerFactory bean using Hibernate
 * implementation.
 * Also we have configured DataSourceInitializer bean to initialize and populate our tables with
 * seed data.
 * We can enable/disable executing this db.sql script by changing init-db property value in
 * application.properties.
 * <p/>
 * And finally optionally we have enabled Spring Data JPA repositories scanning using
 *
 * @EnableJpaRepositories to scan "com.lambo.smartpay.repositories" package for JPA repository
 * interfaces.
 * Created by swang on 2/12/2015.
 */
@Configuration
@EnableTransactionManagement
@Profile("hsql")
@ComponentScan({"com.swang.smartart.core.persistence"})
@PropertySource(value = "classpath:application-hsql.properties", ignoreResourceNotFound = true)
//@EnableJpaRepositories(basePackages = {"com.lambo.smartpay.repositories"}) // not used right now
public class PersistenceConfigHsql {

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceConfigHsql.class);

    @Autowired
    private Environment env;

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LOG.debug("Creating instance of singleton bean '" +
                LocalContainerEntityManagerFactoryBean.class.getName() + "'");
        LocalContainerEntityManagerFactoryBean factory = new
                LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(Boolean.TRUE);
        vendorAdapter.setShowSql(Boolean.TRUE);

        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.swang.smartart.core.persistence.entity");

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.default_schema", env.getProperty("hibernate.default_schema"));
        jpaProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        jpaProperties.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        factory.setJpaProperties(jpaProperties);

        factory.afterPropertiesSet();
        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        return factory;
    }

    @Bean(name = "hibernateExceptionTranslator")
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        LOG.debug("Creating instance of singleton bean '" + EmbeddedDatabaseBuilder.class.getName
                () + "'");
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).setName("smartpay")
                .addScript("classpath:hsql_schema.sql")
                .build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        LOG.debug("Creating instance of singleton bean '" + JpaTransactionManager.class.getName()
                + "'");
        EntityManagerFactory factory = entityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }
}
