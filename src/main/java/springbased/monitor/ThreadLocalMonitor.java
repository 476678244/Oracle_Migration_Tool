package springbased.monitor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadLocalMonitor {
  
  public static AtomicLong getBytesTime = new AtomicLong(0L);
  
  public static AtomicLong setBytesTime = new AtomicLong(0L);
  
  private static final ThreadLocal<Info> info = new ThreadLocal<Info>() {

    @Override
    protected Info initialValue() {
      return new Info();
    }
    
  };
  
  private static final ThreadLocal<Set<String>> uks = new ThreadLocal<Set<String>>();
  
  private static final ThreadLocal<Set<Future<?>>> futures = new ThreadLocal<Set<Future<?>>>(){

    @Override
    protected Set<Future<?>> initialValue() {
      return new HashSet<Future<?>>();
    }

  };
  
  private static final ThreadLocal<ExecutorService> pool = new ThreadLocal<ExecutorService>(){

    @Override
    protected ExecutorService initialValue() {
      return new ThreadPoolExecutor(3, 3, 0L,
          TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }
    
  };

  public static Set<Future<?>> getFutures() {
    return futures.get();
  }
  
  public static void setFutures(Set<Future<?>> futuresToSet) {
    futures.set(futuresToSet);
  }
  
  public static ExecutorService getThreadPool() {
    return pool.get();
  }
  
  public static void setThreadPool(ExecutorService threadPool) {
    pool.set(threadPool);
  }
  
  public static Info getInfo() {
    return info.get();
  }
  
  public static void setInfo(Info infoToSet) {
    info.set(infoToSet);
  }
  
  public static void setUks(Set<String> uksToSet) {
    uks.set(uksToSet);
  }
  
  public static Set<String> getUks() {
    return uks.get();
  }
}
