package com.sumit.holy_attack.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    // Cache configuration is handled by application.properties
    // This class enables caching support
}