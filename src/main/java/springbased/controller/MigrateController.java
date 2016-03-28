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
import springbased.bean.MigrationJob;
import springbased.bean.StatusEnum;
import springbased.bean.ValidationResult;
import springbased.dao.impl.ConnectionInfoDAO;
import springbased.dao.impl.MigrationJobDAO;
import springbased.monitor.ThreadLocalErrorMonitor;
import springbased.readonly.ReadOnlyConnection;
import springbased.service.FKUtil;
import springbased.service.IndexUtil;
import springbased.service.MigrationService;
import springbased.service.SequenceUtil;
import springbased.service.TableUtil;
import springbased.service.idgenerate.IdService;
import springbased.service.taskpool.MigrationRunnable;
import springbased.service.taskpool.MigrationThreadPool;

@RestController
public class MigrateController {

  private static final Logger log = Logger.getLogger(MigrateController.class);

  @Autowired
  private MigrationService migrationService;

  @Autowired
  private ConnectionInfoDAO connectionInfoDAO;

  @Autowired
  private MigrationJobDAO migrationJobDAO;
  
  @Autowired
  private MigrationThreadPool migrationThreadPool;
  
  @Autowired
  private IdService idService;
  
  @RequestMapping("/migrate")
  public void migrate(String sourceUsername, String sourcePassword,
      String sourceUrl, String sourceSchema, String targetUsername,
      String targetPassword, String targetUrl, String targetSchema)
          throws SQLException {
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
    } finally {
      if (sourceCon != null) {
        sourceCon.close();
      }
      if (targetCon != null) {
        targetCon.close();
      }
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
      @RequestParam("sid") String sid, @RequestParam("schema") String schema)
          throws SQLException {
    String url = "jdbc:oracle:thin:@" + ip + ":1521:" + sid;
    ConnectionInfo sourceConInfo = new ConnectionInfo(username, password, url);
    ReadOnlyConnection sourceCon = null;
    try {
      sourceCon = this.migrationService.getReadOnlyConnection(sourceConInfo);
      ValidationResult result = this.migrationService
          .validateSourceSchema(sourceCon, schema);
      if (result.getStatus() == ValidationResult.FAIL) {
        return result;
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
    } finally {
      if (sourceCon != null) {
        sourceCon.close();
      }
    }
  }

  @RequestMapping("/validateTargetConnection")
  public ValidationResult validateTargetConnection(
      @RequestParam("ip") String ip, @RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam("sid") String sid, @RequestParam("schema") String schema)
          throws SQLException {
    String url = "jdbc:oracle:thin:@" + ip + ":1521:" + sid;
    ConnectionInfo targetConInfo = new ConnectionInfo(username, password, url);
    Connection targetCon = null;
    try {
      targetCon = this.migrationService.getConnection(targetConInfo);
      return this.migrationService.validateTargetSchema(targetCon, schema);
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
    } finally {
      if (targetCon != null) {
        targetCon.close();
      }
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
  
  @RequestMapping("/fireMigration")
  public void fireMigration(@RequestParam("sourceUsername") String sourceUsername,
      @RequestParam("sourcePassword") String sourcePassword,
      @RequestParam("sourceUrl") String sourceUrl, 
      @RequestParam("sourceSchema") String sourceSchema,
      @RequestParam("targetUsername") String targetUsername,
      @RequestParam("targetPassword") String targetPassword,
      @RequestParam("targetUrl") String targetUrl, 
      @RequestParam("targetSchema") String targetSchema) { 
    ConnectionInfo sourceConInfo = new ConnectionInfo(sourceUsername,
        sourcePassword, sourceUrl);
    List<ConnectionInfo> connections = this.connectionInfoDAO.getAll();
    if (!connections.contains(sourceConInfo)) {
      this.connectionInfoDAO.save(sourceConInfo);
      connections = this.connectionInfoDAO.getAll();
    } 
    sourceConInfo = connections.get(connections.indexOf(sourceConInfo));
    ConnectionInfo targetConInfo = new ConnectionInfo(targetUsername,
        targetPassword, targetUrl);
    if (!this.connectionInfoDAO.getAll().contains(targetConInfo)) {
      this.connectionInfoDAO.save(targetConInfo);
      connections = this.connectionInfoDAO.getAll();
    }
    targetConInfo = connections.get(connections.indexOf(targetConInfo));
    MigrationJob job = new MigrationJob();
    job.setJobId(this.idService.generateNextJobId());
    job.setSource(sourceConInfo);
    job.setSourceSchema(sourceSchema);
    job.setTarget(targetConInfo);
    job.setStatus(StatusEnum.TASK_FIRED);
    job.setTargetSchema(targetSchema);
    this.migrationJobDAO.save(job);
    this.migrationThreadPool.addTask(job);
  }
  
  @RequestMapping("/getJobList")
  public List<MigrationJob> getJobList() {
    List<MigrationJob> dbJobList = (List<MigrationJob>) this.migrationJobDAO.getAll();
    List<MigrationJob> jobList = new ArrayList<MigrationJob>(dbJobList.size());
    for (MigrationJob job : dbJobList) {
      MigrationRunnable activeThread = this.migrationThreadPool.getThreadInfo(job.getJobId());
      if (activeThread != null) {
        dbJobList.remove(job);
        jobList.add(job);
      }
    }
    jobList.addAll(dbJobList);
    return jobList;
  }
}
