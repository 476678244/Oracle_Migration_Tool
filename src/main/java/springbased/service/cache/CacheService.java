package springbased.service.cache;

import org.springframework.stereotype.Service;
import springbased.bean.ConnectionInfo;

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

    public String generateCacheKey(ConnectionInfo connectionInfo) {
        return new CacheKey().setConnectionInfo(connectionInfo).generateKey();
    }

    public String generateCacheKey(String schema, ConnectionInfo connectionInfo) {
        return new CacheKey().setSchema(schema).setConnectionInfo(connectionInfo).generateKey();
    }

    public class CacheKey {
        private ConnectionInfo connectionInfo;
        private String schema;

        public CacheKey setConnectionInfo(ConnectionInfo connectionInfo) {
            this.connectionInfo = connectionInfo;
            return this;
        }

        public CacheKey setSchema(String schema) {
            this.schema = schema;
            return this;
        }

        public String generateKey() {
            String key = " | ";
            if (connectionInfo != null) {
                key += connectionInfo.toString() + " | ";
            }
            if (schema != null) {
                key += schema + " | ";
            }
            return key;
        }
    }
}
