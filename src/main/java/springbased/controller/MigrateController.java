package springbased.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import springbased.bean.ConnectionInfo;
import springbased.dao.impl.ConnectionInfoDAO;
import springbased.dao.impl.MigrationJobDAO;
import springbased.service.IndexUtil;
import springbased.service.MigrationService;
import springbased.service.TableUtil;

@RestController
public class MigrateController {

  private static final Logger log = Logger.getLogger(MigrateController.class);

  @Autowired
  private ConnectionInfoDAO connectionInfoDAO;

  @Autowired
  private MigrationJobDAO migrationJobDAO;

  @Autowired
  private MigrationService migrationService;

  public void migrate(String sourceUsername, String sourcePassword,
      String sourceUrl, String sourceSchema, String targetUsername,
      String targetPassword, String targetUrl, String targetSchema) {
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
    } catch (ClassNotFoundException e) {
      log.error(e);
    }
    ConnectionInfo sourceConInfo = new ConnectionInfo(sourceUsername,
        sourcePassword, sourceUrl);
    ConnectionInfo targetConInfo = new ConnectionInfo(targetUsername,
        targetPassword, targetUrl);
    Connection sourceCon = this.migrationService.getConnection(sourceConInfo);
    Connection targetCon = this.migrationService.getConnection(targetConInfo);
    log.info(sourceCon);
    List<String> tableList = new ArrayList<String>();
    TableUtil.fetchDDLAndCopyData(targetCon, targetSchema, sourceCon,
        sourceSchema, tableList);
    IndexUtil.copyIndex(targetCon, targetSchema, sourceCon, sourceSchema,
        tableList);
    int a = 0;

  }
}
