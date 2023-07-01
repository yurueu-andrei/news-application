package ru.clevertec.news.cache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.clevertec.news.annotation.conditional.ConditionalOnCorrectCacheProperties;
import ru.clevertec.news.cache.Cache;
import ru.clevertec.news.cache.factory.CacheFactory;

@Configuration
@ConditionalOnCorrectCacheProperties
@Profile("dev")
public class MyCacheConfig {

    @Value("${cache.algorithm}")
    private String cacheType;
    @Value("${cache.size}")
    private int cacheSize;

    @Bean(name = "myCache")
    public Cache createBeanFromNonStaticMethodFactory() {
        CacheFactory factory = new CacheFactory();
        return factory.createCache(cacheType, cacheSize);
    }
}
