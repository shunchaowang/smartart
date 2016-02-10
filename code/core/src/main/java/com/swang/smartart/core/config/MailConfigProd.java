package com.swang.smartart.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configure common service layer beans such as PropertySourcesPlaceholderConfigurer,
 * JavaMailSender and CacheManager.
 * We need to exclude scanning of ecs.* package otherwise the
 * java.lang.IllegalArgumentException: A ServletContext is required to configure default servlet
 * handling will be
 * thrown when running junit test.
 * <p/>
 * Created by swang on 2/12/2015.
 */
@Configuration
@Profile("prod")
@PropertySource(value = "classpath:application-prod.properties", ignoreResourceNotFound = true)
public class MailConfigProd {

    private static final Logger LOG = LoggerFactory.getLogger(MailConfigProd.class);

    @Autowired
    private Environment env;

    @Bean(name = "javaMailSender")
    public JavaMailSenderImpl javaMailSender() {
        LOG.debug("Creating instance of singleton bean '" + JavaMailSenderImpl.class.getName() +
                "'");
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("smtp.host"));
        mailSender.setPort(env.getProperty("smtp.port", Integer.class));
        mailSender.setProtocol(env.getProperty("smtp.protocol"));
        mailSender.setUsername(env.getProperty("smtp.username"));
        mailSender.setPassword(env.getProperty("smtp.password"));

        Properties javaMailProps = new Properties();
        javaMailProps.put("mail.smtp.auth", true);
        javaMailProps.put("mail.smtp.starttls.enable", true);

        mailSender.setJavaMailProperties(javaMailProps);

        return mailSender;
    }
}
