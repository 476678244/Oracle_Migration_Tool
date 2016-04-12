package springbased.monitor;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ThreadLocalErrorMonitor {
  
  private static final ThreadLocal<List<SQLError>> monitor = new ThreadLocal<List<SQLError>>();
  
  private static List<SQLError> getMonitor() {
    List<SQLError> obj = monitor.get();
    if (obj == null) {
      monitor.set(new LinkedList<SQLError>());
      return monitor.get();
    }
    return obj;
  }
  
  public static void add(SQLError error) {
    getMonitor().add(error);
  }
  
  public static void add(String sql, SQLException e) {
    SQLError error = new SQLError();
    error.setSql(sql);
    error.setExceptionMessage(e.getMessage());
    ThreadLocalErrorMonitor.add(error);
  }
  
  public static String printErrors () {
    return getMonitor().toString();
  }
  
  public static boolean isErrorsExisting() {
    return !monitor.get().isEmpty();
  }
  
  public static boolean isDebugMode() {
    return true;
  }
}
