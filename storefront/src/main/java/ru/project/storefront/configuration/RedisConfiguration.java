package ru.project.storefront.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.project.storefront.dto.CachedItem;
import ru.project.storefront.dto.CachedItems;

@Configuration
@EnableCaching
public class RedisConfiguration {

    @Bean
    public ReactiveRedisTemplate<String, CachedItem> itemRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,
                                                                       ObjectMapper objectMapper) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<CachedItem> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, CachedItem.class);
        RedisSerializationContext<String, CachedItem> context = RedisSerializationContext
                .<String, CachedItem>newSerializationContext(keySerializer)
                .value(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, CachedItems> cachedItemsRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper) {

        StringRedisSerializer keySerializer = new StringRedisSerializer();

        Jackson2JsonRedisSerializer<CachedItems> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, CachedItems.class);

        RedisSerializationContext<String, CachedItems> context =
                RedisSerializationContext
                        .<String, CachedItems>newSerializationContext(keySerializer)
                        .value(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }

}