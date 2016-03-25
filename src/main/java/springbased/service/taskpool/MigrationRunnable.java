package springbased.service.taskpool;

import java.util.Date;

import springbased.bean.ConnectionInfo;
import springbased.bean.StatusEnum;

public interface MigrationRunnable extends Runnable {

  ConnectionInfo getSourceConnectionInfo ();
  
  ConnectionInfo getTargetConnectionInfo ();
  
  StatusEnum getStatus();
  
  Date getStartTime();
  
}
