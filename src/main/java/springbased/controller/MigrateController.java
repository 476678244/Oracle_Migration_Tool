package springbased.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import springbased.bean.ConnectionInfo;
import springbased.monitor.ThreadLocalErrorMonitor;
import springbased.readonly.ReadOnlyConnection;
import springbased.service.FKUtil;
import springbased.service.IndexUtil;
import springbased.service.MigrationService;
import springbased.service.SequenceUtil;
import springbased.service.TableUtil;

@RestController
public class MigrateController {

  private static final Logger log = Logger.getLogger(MigrateController.class);

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
    ReadOnlyConnection sourceCon = this.migrationService
        .getReadOnlyConnection(sourceConInfo);
    Connection targetCon = this.migrationService.getConnection(targetConInfo);
    if (!sourceCon.isReadOnly()) {
      log.warn("sourceCon account is not read only...");
    }
    List<String> tableList = new ArrayList<String>();
    try {
      TableUtil.fetchDDLAndCopyData(targetCon, targetSchema, sourceCon,
          sourceSchema, tableList);
      IndexUtil.copyIndex(targetCon, targetSchema, sourceCon, sourceSchema,
          tableList);
      SequenceUtil.copySequence(targetCon, targetSchema, sourceCon,
          sourceSchema, tableList);
      FKUtil.addFK(sourceSchema, targetSchema, sourceCon, targetCon);
    } catch (SQLException sqle) {
      log.error("Migration process failed due to:");
      log.error(sqle);
    }
    if (ThreadLocalErrorMonitor.isErrorsExisting()) {
      log.info("Migration process end successfully, but with some errors.");
      log.info("Please modify and rerun these sqls to fix these errors manually. ");
      log.info(ThreadLocalErrorMonitor.printErrors());
    } else {
      log.error("Migration process end successfully without any errors!");
    }
    int a = 0;

  }
}
