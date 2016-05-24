package springbased.service.taskpool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springbased.bean.MigrationJob;
import springbased.dao.impl.MigrationJobDAO;
import springbased.service.MigrationService;

@Service
public class MigrationThreadPool {
  
  @Autowired
  private MigrationService migrationService;
  
  @Autowired
  private MigrationJobDAO migrationJobDAO;

  private static ExecutorService executorService = Executors
      .newFixedThreadPool(3);

  private static Map<Long, MigrationRunnable> threadMapping =
      new ConcurrentHashMap<Long, MigrationRunnable>();
  
  public void addTask(MigrationJob job) {
    MigrationRunnable thread = new MigrationThread(
        job, this.migrationService, this.migrationJobDAO);
    threadMapping.put(job.getJobId(), thread);
    Future<?> future = executorService.submit(thread);
    thread.setFuture(future);
  }
  
  public MigrationRunnable getThreadInfo(long jobId) {
    return threadMapping.get(jobId);
  }
  
  public void cancelJob(long jobId) {
    if (threadMapping.get(jobId) != null) {      
      threadMapping.get(jobId).cancelJob();
    }
  }
  
  public void shutdown() {
    executorService.shutdown();
  }
}
