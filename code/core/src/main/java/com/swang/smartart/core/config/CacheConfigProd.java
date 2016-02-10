package com.swang.smartart.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by swang on 2/20/2015.
 */
@Configuration
@Profile("prod")
public class CacheConfigProd {

    private static final Logger LOG = LoggerFactory.getLogger(CacheConfigProd.class);

    // use concurrent cache manager for now
    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        LOG.debug("Creating instance of singleton bean '" + ConcurrentMapCacheManager.class
                .getName() + "'");
        return new ConcurrentMapCacheManager();
    }

//    @Bean
//    public CacheManager cacheManager() {
//        LOG.debug("Cache manager is EhCacheManager in prod profile.");
//        return new EhCacheCacheManager(ehCacheManager().getObject());
//    }
//
//    public EhCacheManagerFactoryBean ehCacheManager() {
//        EhCacheManagerFactoryBean ehCache = new EhCacheManagerFactoryBean();
//        ehCache.setConfigLocation(new ClassPathResource("ehcache.xml"));
//        ehCache.setShared(true);
//        return ehCache;
//    }
}
