package springbased.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbased.bean.ConnectionInfo;
import springbased.service.ManageDataQueryService;

/**
 * Created by I303152 on 7/12/2016.
 */
@Service
public class SyncService {

    @Autowired
    private ManageDataQueryService queryService;

    @Autowired
    private CacheService cacheService;

    public void syncSchemasToCache(ConnectionInfo connectionInfo) {
        this.cacheService.addCache(CacheTypeEnum.SCHEMANAMES,
                this.cacheService.generateCacheKey(connectionInfo),
                this.queryService.querySchemas(connectionInfo));
    }

    public void syncTablesToCache(String schema, ConnectionInfo connectionInfo) {
        this.cacheService.addCache(CacheTypeEnum.TABLENAMES,
                this.cacheService.generateCacheKey(schema, connectionInfo),
                this.queryService.queryTableNames(schema, connectionInfo));
    }

}
