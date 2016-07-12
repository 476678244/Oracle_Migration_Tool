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
                CacheTypeEnum.SCHEMANAMES.generateKey(connectionInfo),
                this.queryService.querySchemas(connectionInfo));
    }

    public void syncTablesToCache(String schema, ConnectionInfo connectionInfo) {
        this.cacheService.addCache(CacheTypeEnum.TABLENAMES,
                CacheTypeEnum.TABLENAMES.generateKey(connectionInfo),
                this.queryService.queryTableNames(schema, connectionInfo));
    }

}
