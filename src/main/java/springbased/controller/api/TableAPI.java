package springbased.controller.api;

import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by I303152 on 7/1/2016.
 */
@RestController
public class TableAPI {

    private static final Logger log = Logger.getLogger(TableAPI.class);

    @RequestMapping("/table")
    public List<String> tables(@RequestParam(value = "schema", defaultValue = "") String schema) {
        List<String> tables = new ArrayList<>();
        tables.add("rbp_perm_role");
        tables.add("rbp_perm_rule");
        tables.add("users_group");
        tables.add("users_sysinfo");
        tables.add("usrgrp_map");
        tables.add("permission");
        return tables;
    }

    @RequestMapping("/column")
    public List<String> columns(@RequestParam(value = "schema", defaultValue = "") String schema,
                                @RequestParam(value = "table", defaultValue = "") String table) {
        List<String> columns = new ArrayList<>();
        columns.add("role_id");
        columns.add("role_name");
        columns.add("description");
        columns.add("last_modified_date");
        return columns;
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
    public String tableDataInJson(@RequestParam(value = "schema", defaultValue = "") String schema,
                                  @RequestParam(value = "table", defaultValue = "") String table,
                                  @RequestParam(value = "column", defaultValue = "") String column,
                                  @RequestParam(value = "columnOperator", defaultValue = "") String columnOperator,
                                  @RequestParam(value = "value", defaultValue = "") String value) {
        Map<String, Object> data = new HashMap<String, Object>() ;
        data.put("role_id", 1);
        data.put("role_name", "role1");
        data.put("description", "role1 description");
        data.put("last_modified_date", new Date());
        JSONObject json = new JSONObject(data);
        return json.toString();
    }
}
