package com.lab.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.common.builder.ObjectMapperBuilder;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@SpringBootConfiguration
public class BaseConfig {

//    @Bean
//    public MethodValidationPostProcessor methodValidationPostProcessor() {
//        return new MethodValidationPostProcessor();
//    }

    /**
     * 配置 Spring 的 Json 序列化格式；
     * 该 Bean 理论上属于 Web 层的配置，但为了方便和 redis 的 ObjectMapper 对比，统一放到此处
     *
     * @return Jackson 的消息转换器
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = ObjectMapperBuilder.builder()
            .longToString(true) // web 层的序列化器需要把 long 转为 string 以处理 js 的 long 精度丢失问题
            .build();

        // 为消息转化器设置 ObjectMapper，即表示 spring 将采用该 objectMapper 进行 json 处理
        messageConverter.setObjectMapper(objectMapper);

        // 返回消息转化器
        return messageConverter;
    }

    /**
     * 配置 RedisTemplate，主要是配置序列化方式；
     * 特别注意值采用 json 序列化方式后，对于值类型为 long 类型时有个大坑，存储为 long，反序列化时会被看做 int
     *
     * @param factory redis 配置工厂
     * @param <V>     值类型泛型
     * @return redis 配置模板
     */
    @Bean
    public <V> RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory) {

        // 参照 StringRedisTemplate 的构造器设置配置自定义的 RedisTemplate

        RedisTemplate<String, V> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        // 使用 Jackson2JsonRedisSerializer 来序列化和反序列化 redis 的 value 值（默认使用 JDK 的序列化方式）
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 全部采用 ObjectMapperBuilder 默认值的 builder
        ObjectMapper objectMapper = ObjectMapperBuilder.builder().saveType(true).build();
        serializer.setObjectMapper(objectMapper);

        // key 使用 StringRedisSerializer 来序列化和反序列化
        template.setKeySerializer(new StringRedisSerializer());

        // 值采用 json 序列化
        template.setValueSerializer(serializer);

        // 设置 hash key 和 value 序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();

        return template;
    }

}
