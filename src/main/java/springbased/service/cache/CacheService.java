package springbased.service.cache;

import org.springframework.stereotype.Service;

/**
 * Created by I303152 on 7/12/2016.
 */
@Service
public class CacheService {

    public void addCache(CacheTypeEnum type, String key, Object value) {
        type.add(key, value);
    }

    public Object getCache(CacheTypeEnum type, String key) {
        return type.get(key);
    }

    public void clearCache(CacheTypeEnum type, String key) {
        type.clear(key);
    }
}
