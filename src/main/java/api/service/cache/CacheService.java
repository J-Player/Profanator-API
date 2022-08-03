package api.service.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static api.config.cache.CustomKeyGenerator.generateCustomKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;

    @Getter
    private final Duration durationTTL;

    public final void evictCache(String cacheName, String methodName, Object... params) {
        String simpleName = this.getClass().getSimpleName();
        String key = generateCustomKey(simpleName, methodName, params);
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) return;
        boolean ifPresent = cache.evictIfPresent(key);
        if (ifPresent) log.debug("Cache '{}' foi removido com sucesso!", key);
    }

    public final void evictAllCache() {
        cacheManager.getCacheNames().forEach(this::evictAllCache);
    }

    public final void evictAllCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) return;
        cache.clear();
    }

}
