package com.swang.smartart.manage.config;

import com.swang.smartart.core.config.CoreConfig;
import com.swang.smartart.core.util.PropertiesLoader;
import com.swang.smartart.core.util.ResourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import java.util.Properties;

/**
 * Web application initializer replaces web.xml.
 * Created by swang on 3/12/2015.
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(WebAppInitializer.class);

    private static final PropertiesLoader propertiesLoader = new PropertiesLoader();

    @Override
    protected Class<?>[] getRootConfigClasses() {
        logger.info("Loading core config and application config.");
        return new Class<?>[]{CoreConfig.class, ManageConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        logger.info("Loading mvc config.");
        return new Class<?>[]
                {MvcConfig.class, SecurityConfig.class, CustomMethodSecurityConfig.class};
    }

    @Override
    protected String[] getServletMappings() {

        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        // if encoding has issues we need to add UTF-8 encoding filter
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setForceEncoding(true);
        encodingFilter.setEncoding("UTF-8");
        // encoding filter must be the first one
        return new Filter[]{encodingFilter,
                new DelegatingFilterProxy("springSecurityFilterChain"),
                new OpenEntityManagerInViewFilter()};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        WebApplicationContext context = super.createRootApplicationContext();
        Properties properties = propertiesLoader.load(ResourceProperties.SPRING_PROPERTIES_FILE);
        logger.info("Loading context active profile "
                + properties.getProperty("spring.profiles.active"));
        ((ConfigurableEnvironment) context.getEnvironment())
                .setActiveProfiles(properties.getProperty("spring.profiles.active"));
        return context;
    }

}
