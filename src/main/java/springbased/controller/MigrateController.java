package springbased.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @RequestMapping("/migrate")
  public void migrate(String sourceUsername, String sourcePassword,
      String sourceUrl, String sourceSchema, String targetUsername,
      String targetPassword, String targetUrl, String targetSchema) {
    ConnectionInfo sourceConInfo = new ConnectionInfo(sourceUsername,
        sourcePassword, sourceUrl);
    ConnectionInfo targetConInfo = new ConnectionInfo(targetUsername,
        targetPassword, targetUrl);
    ReadOnlyConnection sourceCon = null;
    Connection targetCon = null;
    try {
      sourceCon = this.migrationService.getReadOnlyConnection(sourceConInfo);
      targetCon = this.migrationService.getConnection(targetConInfo);
    } catch (SQLException e) {
      log.error(e);
      return;
    }
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
      log.info(
          "Please modify and rerun these sqls to fix these errors manually. ");
      log.info(ThreadLocalErrorMonitor.printErrors());
    } else {
      log.error("Migration process end successfully without any errors!");
    }

  }

  @RequestMapping("/validateSourceConnection")
  public ValidationResult validateSourceConnection(
      @RequestParam("ip") String ip, @RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam("sid") String sid, @RequestParam("schema") String schema) {
    String url = "jdbc:oracle:thin:@" + ip + ":1521:" + sid;
    ConnectionInfo sourceConInfo = new ConnectionInfo(username, password, url);
    ReadOnlyConnection sourceCon = null;
    try {
      sourceCon = this.migrationService.getReadOnlyConnection(sourceConInfo);
    } catch (SQLException e) {
      return new ValidationResult() {
        @Override
        public int getStatus() {
          return FAIL;
        }

        @Override
        public String getCause() {
          return e.getMessage();
        }
      };
    }
    if (!sourceCon.isReadOnly()) {
      return new ValidationResult() {
        @Override
        public int getStatus() {
          return SUCCESSWITHWARNING;
        }
        @Override
        public String getCause() {
          return "Source DB connection is not read only.";
        }
      };
    }
    return new ValidationResult();
  }

  @RequestMapping("/validateTargetConnection")
  public ValidationResult validateTargetConnection(
      @RequestParam("ip") String ip, @RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam("sid") String sid, @RequestParam("schema") String schema) {
    String url = "jdbc:oracle:thin:@" + ip + ":1521:" + sid;
    ConnectionInfo targetConInfo = new ConnectionInfo(username, password, url);
    Connection targetCon = null;
    try {
      targetCon = this.migrationService.getConnection(targetConInfo);
    } catch (SQLException e) {
      return new ValidationResult() {
        @Override
        public int getStatus() {
          return FAIL;
        }

        @Override
        public String getCause() {
          return e.getMessage();
        }
      };
    }
    return new ValidationResult();
  }

  public class ValidationResult {
    
    public static final int SUCCESS = 1;
    public static final int FAIL = -1;
    public static final int SUCCESSWITHWARNING = 2;
    public int getStatus() {
      return SUCCESS;
    }

    public String getCause() {
      return "";
    }
  }

  @PostConstruct
  public void init() throws Exception {
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
    } catch (ClassNotFoundException e) {
      log.error(e);
      throw new Exception(e);
    }
  }
}
