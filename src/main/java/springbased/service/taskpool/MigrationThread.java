package springbased.service.taskpool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import springbased.bean.ConnectionInfo;
import springbased.bean.MigrationJob;
import springbased.bean.StatusEnum;
import springbased.dao.impl.MigrationJobDAO;
import springbased.monitor.ThreadLocalErrorMonitor;
import springbased.readonly.ReadOnlyConnection;
import springbased.service.FKUtil;
import springbased.service.IndexUtil;
import springbased.service.MigrationService;
import springbased.service.SequenceUtil;
import springbased.service.TableUtil;

public class MigrationThread implements MigrationRunnable {

  private static final Logger log = Logger.getLogger(MigrationThread.class);

  private MigrationJob job;

  private MigrationService migrationService;
  
  private MigrationJobDAO jobDAO;

  public MigrationThread(MigrationJob job, MigrationService migrationService,
      MigrationJobDAO jobDAO) {
    super();
    this.job = job;
    this.migrationService = migrationService;
    this.jobDAO = jobDAO;
  }

  @Override
  public void run() {
    job.setStatus(StatusEnum.STARTED);
    job.setStartTime(new Date());
    ReadOnlyConnection sourceCon = null;
    Connection targetCon = null;
    try {
      sourceCon = this.migrationService.getReadOnlyConnection(job.getSource());
      targetCon = this.migrationService.getConnection(job.getTarget());
    } catch (SQLException e) {
      log.error(e);
      return;
    }
    List<String> tableList = new ArrayList<String>();
    try {
      TableUtil.fetchDDLAndCopyData(targetCon, job.getTargetSchema(), sourceCon,
          job.getSourceSchema(), tableList);
      IndexUtil.copyIndex(targetCon, job.getTargetSchema(), sourceCon,
          job.getSourceSchema(), tableList);
      SequenceUtil.copySequence(targetCon, job.getTargetSchema(), sourceCon,
          job.getSourceSchema(), tableList);
      FKUtil.addFK(job.getSourceSchema(), job.getTargetSchema(), sourceCon,
          targetCon);
    } catch (SQLException sqle) {
      log.error("Migration process failed due to:");
      log.error(sqle);
    } finally {
      if (sourceCon != null) {
        try {
          sourceCon.close();
        } catch (SQLException e) {
          log.error(e);
        }
      }
      if (targetCon != null) {
        try {
          targetCon.close();
        } catch (SQLException e) {
          log.error(e);
        }
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
    job.setEndTime(new Date());
    job.setStatus(StatusEnum.FINISHED);
    this.jobDAO.save(job);
  }

  @Override
  public ConnectionInfo getSourceConnectionInfo() {
    return job.getSource();
  }

  @Override
  public ConnectionInfo getTargetConnectionInfo() {
    return job.getTarget();
  }

  @Override
  public StatusEnum getStatus() {
    return job.getStatus();
  }

  @Override
  public Date getStartTime() {
    return job.getStartTime();
  }

}
