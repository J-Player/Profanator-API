package api.services.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import static api.configs.cache.CustomKeyGenerator.generateCustomKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;

    public void evictCache(String className, String cacheName, String methodName, Object... params) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) return;
        String key = generateCustomKey(className, methodName, params);
        boolean present = cache.evictIfPresent(key);
        if (present) log.trace("Cache '{}' foi removido com sucesso!", key);
    }

    public void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) return;
        cache.clear();
    }

    public void clearAllCache() {
        cacheManager.getCacheNames().forEach(this::clearCache);
    }

}
