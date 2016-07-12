package springbased.service.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by I303152 on 7/12/2016.
 */
public enum CacheTypeEnum {
    SCHEMANAMES, TABLENAMES;

    public String generateKey(Object keys) {
        return keys.toString();
    }

    private Map<String, Object> caches = new HashMap<>();

    public void add(String key, Object value) {
        caches.put(key, value);
    }

    public Object get(String key) {
        return caches.get(key);
    }

    public void clear(String key) {
        caches.clear();
    }
}
