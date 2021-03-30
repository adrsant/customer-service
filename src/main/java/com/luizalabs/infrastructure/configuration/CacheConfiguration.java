package com.luizalabs.infrastructure.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfiguration {

  public static final String PRODUCT_CACHE_NAME = "products";

  @Value("${cache.expire}")
  private long expire;

  @Value("${cache.maximum-size}")
  private long maximumSize;

  @Bean
  public CacheManager cacheManager(Caffeine caffeine) {
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(PRODUCT_CACHE_NAME);
    caffeineCacheManager.setCaffeine(caffeine);
    return caffeineCacheManager;
  }

  @Bean
  public Caffeine caffeineConfig() {
    return Caffeine.newBuilder()
        .expireAfterWrite(expire, TimeUnit.MINUTES)
        .maximumSize(maximumSize);
  }
}
