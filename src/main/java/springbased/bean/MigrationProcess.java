package springbased.bean;

import java.sql.Connection;

public class MigrationProcess {

  private MigrationJob job;
  
  private Connection sourceConnection;
  
  private Connection targetConnection;
  
  private StatusEnum status;

  public MigrationJob getJob() {
    return job;
  }

  public void setJob(MigrationJob job) {
    this.job = job;
  }

  public Connection getSourceConnection() {
    return sourceConnection;
  }

  public void setSourceConnection(Connection sourceConnection) {
    this.sourceConnection = sourceConnection;
  }

  public Connection getTargetConnection() {
    return targetConnection;
  }

  public void setTargetConnection(Connection targetConnection) {
    this.targetConnection = targetConnection;
  }

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }
  
}
