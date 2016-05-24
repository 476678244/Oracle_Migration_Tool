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
import springbased.monitor.Info;
import springbased.monitor.ThreadLocalErrorMonitor;
import springbased.monitor.ThreadLocalMonitor;
import springbased.service.FKUtil;
import springbased.service.IndexUtil;
import springbased.service.MigrationService;
import springbased.service.SequenceUtil;
import springbased.service.TableUtil;
import springbased.service.UKUtil;

public class MigrationThread extends Thread implements MigrationRunnable {

  private static final Logger log = Logger.getLogger(MigrationThread.class);

  private MigrationJob job;

  private MigrationJobDAO jobDAO;

  private Info info = new Info();

  public MigrationThread(MigrationJob job, MigrationService migrationService,
      MigrationJobDAO jobDAO) {
    super();
    this.job = job;
    this.jobDAO = jobDAO;
  }

  @Override
  public void run() {
    ThreadLocalMonitor.setInfo(info);
    try {
      this.jobDAO.updateStatus(job.getJobId(), StatusEnum.TABLE);
      this.jobDAO.updateStartTime(job.getJobId(), new Date());
      List<String> tableList = new ArrayList<String>();
      try {
        this.jobDAO.updateStatus(job.getJobId(), StatusEnum.TABLE);
        TableUtil.execute(job.getTarget(), job.getTargetSchema(),
            job.getSource(), job.getSourceSchema(), tableList);
        this.jobDAO.updateStatus(job.getJobId(), StatusEnum.UK);
        UKUtil.execute(job.getTarget(), job.getTargetSchema(), job.getSource(),
            job.getSourceSchema());
        this.jobDAO.updateStatus(job.getJobId(), StatusEnum.INDEX);
        IndexUtil.copyIndex(job.getTarget(), job.getTargetSchema(),
            job.getSource(), job.getSourceSchema(), tableList);
        this.jobDAO.updateStatus(job.getJobId(), StatusEnum.SEQUENCE);
        SequenceUtil.copySequence(job.getTarget(), job.getTargetSchema(),
            job.getSource(), job.getSourceSchema(), tableList);
        this.jobDAO.updateStatus(job.getJobId(), StatusEnum.FK);
        FKUtil.addFK(job.getSourceSchema(), job.getTargetSchema(),
            job.getSource(), job.getTarget());
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
      this.jobDAO.updateEndTime(job.getJobId(), new Date());
      this.jobDAO.updateStatus(job.getJobId(), StatusEnum.FINISHED);
    } catch (InterruptedException ie) {
      log.error(ie);
    } catch (Exception e) {
      this.jobDAO.updateStatus(job.getJobId(), StatusEnum.FAILED);
      log.error(e);
    } finally {
      ThreadLocalMonitor.getThreadPool().shutdown();
    }
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

  @Override
  public Info getInfo() {
    return this.info;
  }
}
