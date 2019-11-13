package com.qgailab.authsystem.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @author linxu
 * @date 2019/2/28
 * 采用redis充当高速缓冲层；
 */
@Configuration
@EnableCaching
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * 设置12小时的缓存时间
     */
    private static final Duration TTL_HALF_DAY = Duration.ofHours(12);

    /**
     * 设置30分钟的缓存时间
     */
    private static final Duration TTL_HALF_HOUR = Duration.ofMinutes(30);
    /**
     * 设置15分钟的缓存时间
     */
    private static final Duration A_CLOCK = Duration.ofMinutes(15);

    /**
     * from spring boot -data -redis 2.1.X
     *
     * @param factory f
     * @return cManager
     * 指定缓存管理器
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        //默认
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(TTL_HALF_HOUR)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                .disableCachingNullValues();
        //设置多个缓存空间
        Set<String> cacheNames = new HashSet<>();
        //cacheNames.add("city_map");
        //这是注册暂存区的空间
        cacheNames.add("register_temp_storage");
        //这是口令暂存空间
        cacheNames.add("tokens");

        //为每个缓存空间设置不同配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        //configMap.put("city_map", config.entryTtl(TTL_HALF_DAY));
        configMap.put("register_temp_storage", config);
        configMap.put("tokens", config.entryTtl(A_CLOCK));

        //利用配置构造缓存管理器
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .transactionAware()
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(configMap)
                .build();

        log.info("cacheManager load complete.");
        return redisCacheManager;
    }

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    /**
     * change the default template
     *
     * @param factory f
     * @return tem
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        //change the factory to connect
        redisTemplate.setConnectionFactory(factory);
        //use the jdk serial tool
        JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        //读取后再进行设置..
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
