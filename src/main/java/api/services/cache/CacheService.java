package api.services.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import static api.configs.cache.CustomKeyGenerator.generateCustomKey;
import static java.lang.Thread.currentThread;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;

    public void evictCache(String cacheName, Object... params) {
        StackTraceElement ste = currentThread().getStackTrace()[2];
        String[] strings = ste.getClassName().split("\\.");
        String className = strings[strings.length - 1];
        evictCache(cacheName, className, ste.getMethodName(), params);
    }

    public void evictCache(String cacheName, String methodName, Object... params) {
        StackTraceElement ste = currentThread().getStackTrace()[2];
        String[] strings = ste.getClassName().split("\\.");
        String className = strings[strings.length - 1];
        evictCache(cacheName, className, methodName, params);
    }

    private void evictCache(String cacheName, String className, String methodName, Object... params) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            String key = generateCustomKey(className, methodName, params);
            boolean present = cache.evictIfPresent(key);
            if (present) log.trace("Cache removido com sucesso! (CacheName: {} | Key: {})", cacheName, key);
            else log.trace("Cache não encontrado. (CacheName: {} | Key: {})", cacheName, key);
        } else {
            log.trace("Cache '{}' não existe ou não pode ser criado. (ClassName: {} | MethodName: {})", cacheName, className, methodName);
        }
    }

    public void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null && cache.invalidate()) log.trace("Cache limpo com sucesso! (CacheName: {})", cacheName);
    }

    public void clearAllCache() {
        cacheManager.getCacheNames().forEach(this::clearCache);
    }

}
