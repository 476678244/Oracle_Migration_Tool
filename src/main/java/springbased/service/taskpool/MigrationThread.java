package springbased.service.taskpool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import springbased.bean.ConnectionInfo;
import springbased.bean.MigrationJob;
import springbased.bean.StatusEnum;
import springbased.dao.impl.MigrationJobDAO;
import springbased.monitor.ThreadLocalErrorMonitor;
import springbased.service.FKUtil;
import springbased.service.IndexUtil;
import springbased.service.MigrationService;
import springbased.service.SequenceUtil;
import springbased.service.TableUtil;

public class MigrationThread extends Thread implements MigrationRunnable {

  private static final Logger log = Logger.getLogger(MigrationThread.class);

  private MigrationJob job;
  
  private MigrationJobDAO jobDAO;

  public MigrationThread(MigrationJob job, MigrationService migrationService,
      MigrationJobDAO jobDAO) {
    super();
    this.job = job;
    this.jobDAO = jobDAO;
  }

  @Override
  public void run() {
    job.setStatus(StatusEnum.STARTED);
    job.setStartTime(new Date());
    this.jobDAO.save(job);
    List<String> tableList = new ArrayList<String>();
    try {
      job.setStatus(StatusEnum.TABLE);
      this.jobDAO.save(job);
      TableUtil.fetchDDLAndCopyData(job.getTarget(), job.getTargetSchema(), job.getSource(),
          job.getSourceSchema(), tableList);
      job.setStatus(StatusEnum.INDEX);
      this.jobDAO.save(job);
      IndexUtil.copyIndex(job.getTarget(), job.getTargetSchema(), job.getSource(),
          job.getSourceSchema(), tableList);
      job.setStatus(StatusEnum.SEQUENCE);
      this.jobDAO.save(job);
      SequenceUtil.copySequence(job.getTarget(), job.getTargetSchema(), job.getSource(),
          job.getSourceSchema(), tableList);
      job.setStatus(StatusEnum.FK);
      this.jobDAO.save(job);
      FKUtil.addFK(job.getSourceSchema(), job.getTargetSchema(), job.getSource(),
          job.getTarget());
    } catch (SQLException sqle) {
      log.error("Migration process failed due to:");
      log.error(sqle);
    } finally {
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

  private Future<?> future;
  
  @Override
  public void setFuture(Future<?> future) {
    this.future = future;
  }

  @Override
  public void cancelJob() {
    if (!this.future.isCancelled()) {
      this.future.cancel(true);
    }
  }

  @Override
  public boolean isDone() {
    return this.future.isDone();
  }
}
