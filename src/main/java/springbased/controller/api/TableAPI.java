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

    @RequestMapping("/table")
    public List<String> tables(@RequestParam("sourceUsername") String sourceUsername,
                               @RequestParam("sourcePassword") String sourcePassword,
                               @RequestParam("sourceUrl") String sourceUrl,
                               @RequestParam(value = "schema", defaultValue = "") String schema,
                               @RequestParam(value = "key", defaultValue = "") String key) {
        List<String> tables = this.queryService.queryTableNames(
                schema, new ConnectionInfo(sourceUsername, sourcePassword, sourceUrl));
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
                                  @RequestParam(value = "column", defaultValue = "") String column,
                                  @RequestParam(value = "columnOperator", defaultValue = "") String columnOperator,
                                  @RequestParam(value = "value", defaultValue = "") String value,
                                  @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        List<Object> bindVars = new ArrayList<>();
        if (!StringUtils.isBlank(value)) {
            bindVars.add(value);
        }
        List<Map<String, Object>> list = this.queryService.query(
                this.queryService.toSql(schema, table, column, columnOperator, value, orderBy),
                new ConnectionInfo(sourceUsername, sourcePassword, sourceUrl), bindVars.toArray());
        //JSONObject json = new JSONObject(list)
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> map : list) {
            JSONObject jsonMap = new JSONObject(map);
            jsonArray.put(jsonMap);
        }
        return jsonArray.toString();
    }
}
