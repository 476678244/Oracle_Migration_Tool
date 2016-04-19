package springbased.monitor;

import java.util.Set;

public class ThreadLocalMonitor {

  private static final ThreadLocal<Info> info = new ThreadLocal<Info>();
  
  private static final ThreadLocal<Set<String>> uks = new ThreadLocal<Set<String>>();
  
  public static Info getInfo() {
    Info obj = info.get();
//    if (obj == null) {
//      info.set(new Info());
//      return info.get();
//    }
    return obj;
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
