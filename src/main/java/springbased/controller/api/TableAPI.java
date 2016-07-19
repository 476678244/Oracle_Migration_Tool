package springbased.controller.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.collect.Collections2;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springbased.bean.ConnectionInfo;
import springbased.service.ManageDataQueryService;
import springbased.service.cache.CacheService;
import springbased.service.cache.CacheTypeEnum;
import springbased.service.cache.SyncService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by I303152 on 7/1/2016.
 */
@RestController
public class TableAPI {

    private static final Logger log = Logger.getLogger(TableAPI.class);

    @Autowired
    private ManageDataQueryService queryService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SyncService syncService;

    @RequestMapping("/table")
    public List<String> tables(@RequestParam("sourceUsername") String sourceUsername,
                               @RequestParam("sourcePassword") String sourcePassword,
                               @RequestParam("sourceUrl") String sourceUrl,
                               @RequestParam(value = "schema", defaultValue = "") String schema,
                               @RequestParam(value = "key", defaultValue = "") String key) {
        ConnectionInfo connectionInfo = new ConnectionInfo(sourceUsername, sourcePassword, sourceUrl);
        List<String> tables = (List<String>)this.cacheService.getCache(
                CacheTypeEnum.TABLENAMES, this.cacheService.generateCacheKey(schema, connectionInfo));
        if (tables == null) {
            new Thread(()-> {
                this.syncService.syncTablesToCache(schema, connectionInfo);
            }).start();
            tables = this.queryService.queryTableNames(schema, connectionInfo, key);
        }
        return tables.stream().filter(
                table -> table.toUpperCase().contains(key.toUpperCase())).collect(Collectors.toList());
    }

    @RequestMapping("/column")
    public List<String> columns(@RequestParam("sourceUsername") String sourceUsername,
                                @RequestParam("sourcePassword") String sourcePassword,
                                @RequestParam("sourceUrl") String sourceUrl,
                                @RequestParam(value = "schema", defaultValue = "") String schema,
                                @RequestParam(value = "table", defaultValue = "") String table) {
        return this.queryService.queryColumnNames(
                schema, table, new ConnectionInfo(sourceUsername, sourcePassword, sourceUrl));
    }

    @RequestMapping("/columnOperator")
    public List<String> columnOperators() {
        List<String> operators = new ArrayList<>();
        operators.add("<");
        operators.add(">");
        operators.add("=");
        operators.add("like");
        return operators;
    }

    @RequestMapping("/tableDataInJson")
    public String tableDataInJson(@RequestParam("sourceUsername") String sourceUsername,
                                  @RequestParam("sourcePassword") String sourcePassword,
                                  @RequestParam("sourceUrl") String sourceUrl,
                                  @RequestParam(value = "schema", defaultValue = "") String schema,
                                  @RequestParam(value = "table", defaultValue = "") String table,
                                  @RequestParam(value = "whereSql", defaultValue = "") String whereSql,
                                  @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                  @RequestParam(value = "selectColumns", defaultValue = "") String selectColumns) {
        List<Object> bindVars = new ArrayList<>();
        List<Map<String, Object>> list = this.queryService.query(
                this.queryService.toSql(schema, table, whereSql, orderBy, selectColumns),
                new ConnectionInfo(sourceUsername, sourcePassword, sourceUrl), bindVars.toArray());
        if (selectColumns.contains("distinct") && list.size() > 29) {
            list = list.subList(0, 29);
        }
        //JSONObject json = new JSONObject(list)
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> map : list) {
            JSONObject jsonMap = new JSONObject(map);
            jsonArray.put(jsonMap);
        }
        return jsonArray.toString();
    }
}
