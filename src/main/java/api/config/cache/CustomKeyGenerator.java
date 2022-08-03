package api.config.cache;

import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Locale;

@Component
public class CustomKeyGenerator extends SimpleKeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return generateCustomKey(target.getClass().getSimpleName(), method.getName(), params);
    }

    public static String generateCustomKey(String className, String methodName, Object... params) {
        String key = className + "_" + methodName;
        if (params != null && params.length > 0) key += "_" + StringUtils.arrayToDelimitedString(params, "_");
        return key.replaceAll("[\\W]+", "_").toLowerCase(Locale.ROOT);
    }

}
