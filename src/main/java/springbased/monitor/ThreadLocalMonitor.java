package springbased.monitor;

public class ThreadLocalMonitor {

  private static final ThreadLocal<Info> info = new ThreadLocal<Info>();
  
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
}
