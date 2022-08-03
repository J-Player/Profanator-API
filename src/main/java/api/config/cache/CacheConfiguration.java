package api.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Slf4j
@EnableCaching
@Configuration
public class CacheConfiguration {

    public static final String PROFICIENCY_CACHE_NAME = "proficiencies";
    public static final String ITEM_CACHE_NAME = "items";
    public static final String INGREDIENT_CACHE_NAME = "ingredients";

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(List.of(
                new ConcurrentMapCache(PROFICIENCY_CACHE_NAME),
                new ConcurrentMapCache(ITEM_CACHE_NAME),
                new ConcurrentMapCache(INGREDIENT_CACHE_NAME)
        ));
        return simpleCacheManager;
    }

    @Bean
    public Duration durationTTL(@Value("${app.config.cache.ttl:60s}") String ttl) {
        final boolean b = ttl.matches("^([\\d]+)[dhms]{1}");
        if (!b) {
            log.warn("A propriedade app.config.cache.ttl nao esta correta. Sintaxe: <numero><d|h|m|s>.");
            log.info("Definindo valor padrao para TTL -> 60s");
            return Duration.ofSeconds(60);
        }
        final String type = ttl.substring(ttl.length() - 1);
        final long num = Long.parseLong(ttl.substring(0, ttl.length() - 1));
        switch (type) {
            case "d":
                return Duration.ofDays(num);
            case "h":
                return Duration.ofHours(num);
            case "m":
                return Duration.ofMinutes(num);
            default:
                return Duration.ofSeconds(num);
        }
    }

}
