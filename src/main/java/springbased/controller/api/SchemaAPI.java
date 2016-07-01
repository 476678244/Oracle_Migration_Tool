package springbased.controller.api;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I303152 on 7/1/2016.
 */
@RestController
public class SchemaAPI {

    private static final Logger log = Logger.getLogger(SchemaAPI.class);

    @RequestMapping("/schema")
    public List<String> schemas() {
        List<String> schemas = new ArrayList<>();
        schemas.add("sfuser_tree");
        schemas.add("sfuser_temp");
        schemas.add("sfuser_real");
        return schemas;
    }
}
