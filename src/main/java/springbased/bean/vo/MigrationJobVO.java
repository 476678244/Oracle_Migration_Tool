package springbased.bean.vo;

import springbased.bean.MigrationJob;
import springbased.monitor.Info;

public class MigrationJobVO {

  public MigrationJobVO(MigrationJob job) {
    this.job = job;
  }
  
  private MigrationJob job;
  
  private Info info = new Info();

  public Info getInfo() {
    return info;
  }

  public void setInfo(Info info) {
    this.info = info;
  }

  public MigrationJob getJob() {
    return job;
  }

  public void setJob(MigrationJob job) {
    this.job = job;
  }
  
  public long getStartTime() {
    if (job.getStartTime() == null) return 0;
    return job.getStartTime().getTime();
  }
  
  public long getEndTime() {
    if (job.getEndTime() == null) return 0;
    return job.getEndTime().getTime();
  }
  
  private boolean inThreadPool = false;

  public boolean isInThreadPool() {
    return inThreadPool;
  }

  public void setInThreadPool(boolean inThreadPool) {
    this.inThreadPool = inThreadPool;
  }
  
  
}
