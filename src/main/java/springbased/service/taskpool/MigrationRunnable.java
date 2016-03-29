package springbased.service.taskpool;

import java.util.Date;
import java.util.concurrent.Future;

import springbased.bean.ConnectionInfo;
import springbased.bean.StatusEnum;

public interface MigrationRunnable extends Runnable {

  ConnectionInfo getSourceConnectionInfo ();
  
  ConnectionInfo getTargetConnectionInfo ();
  
  StatusEnum getStatus();
  
  Date getStartTime();
  
  void setFuture(Future<?> future);
  
  Future<?> getFuture();
}
