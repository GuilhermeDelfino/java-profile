package narciso.guilherme.github.profile.configuration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import narciso.guilherme.github.profile.input.UserQueryCacheService;
import narciso.guilherme.github.profile.input.dto.UserResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;

@Configuration
public class RedisConfig {

  private static final String CACHE_PREFIX = "dto-cache-v2::";

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    RedisCacheConfiguration baseConfig = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(10))
        .computePrefixWith(cacheName -> CACHE_PREFIX + cacheName + "::")
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new StringRedisSerializer()));

    Jackson2JsonRedisSerializer<UserResponse> userResponseSerializer =
        new Jackson2JsonRedisSerializer<>(objectMapper, UserResponse.class);

    JavaType userResponseListType =
        objectMapper.getTypeFactory().constructCollectionType(List.class, UserResponse.class);
    Jackson2JsonRedisSerializer<List<UserResponse>> userResponseListSerializer =
        new Jackson2JsonRedisSerializer<>(objectMapper, userResponseListType);

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(baseConfig)
        .withCacheConfiguration(
            UserQueryCacheService.USER_BY_ID_CACHE,
            baseConfig.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    userResponseSerializer)))
        .withCacheConfiguration(
            UserQueryCacheService.USER_LIST_CACHE,
            baseConfig.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    userResponseListSerializer)))
        .build();
  }
}
