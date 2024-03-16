package api.configs.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Slf4j
@Configuration
//@EnableCaching //FIXME: Cache não está funcionando corretamente para retornos paginados.
public class CacheConfig implements CachingConfigurer {

    public static final String PROFICIENCY_CACHE_NAME = "proficiencies";
    public static final String ITEM_CACHE_NAME = "items";
    public static final String INGREDIENT_CACHE_NAME = "ingredients";
    public static final Duration TTL = Duration.ofSeconds(30);

    @Bean
    @Override
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(List.of(
                new ConcurrentMapCache(PROFICIENCY_CACHE_NAME, false),
                new ConcurrentMapCache(ITEM_CACHE_NAME, false),
                new ConcurrentMapCache(INGREDIENT_CACHE_NAME, false)
        ));
        return simpleCacheManager;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }

}
