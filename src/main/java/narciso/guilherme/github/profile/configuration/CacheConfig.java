package narciso.guilherme.github.profile.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

  @Bean
  public CacheErrorHandler cacheErrorHandler() {
    return new CacheErrorHandler() {
      @Override
      public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.warn("Ignoring cache get error on cache={} key={}", cache.getName(), key, exception);
        safeEvict(cache, key);
      }

      @Override
      public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.warn("Ignoring cache put error on cache={} key={}", cache.getName(), key, exception);
      }

      @Override
      public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.warn("Ignoring cache evict error on cache={} key={}", cache.getName(), key, exception);
      }

      @Override
      public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.warn("Ignoring cache clear error on cache={}", cache.getName(), exception);
      }

      private void safeEvict(Cache cache, Object key) {
        try {
          cache.evict(key);
        } catch (RuntimeException evictException) {
          log.warn("Failed to evict bad cache entry on cache={} key={}", cache.getName(), key, evictException);
        }
      }
    };
  }
}
