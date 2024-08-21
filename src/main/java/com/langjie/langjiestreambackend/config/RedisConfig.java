package com.langjie.langjiestreambackend.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/8/16 16:40
 **/

@Configuration
public class RedisConfig {

    /**
    * @Author: ZooMEISTER
    * @Description: 该类主要用于配置key-value序列化和反序列化。
    * @DateTime: 2024/8/16 16:41
    * @Params: [factory]
    * @Return org.springframework.data.redis.core.RedisTemplate<java.lang.Object,java.lang.Object>
    */
    @Primary
    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        template.setConnectionFactory(factory);
        //key序列化方式
        template.setKeySerializer(redisSerializer);
        //value序列化
        template.setValueSerializer(redisSerializer);
        //value Hashmap序列化
        template.setHashValueSerializer(redisSerializer);
        //key Hashmap序列化
        template.setHashKeySerializer(redisSerializer);

        return template;
    }
}