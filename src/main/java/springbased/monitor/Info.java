package springbased.monitor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Info {

  private String processRate = "";
  
  private String tableUtilState = "";

  private Set<String> tablesWithBlobClobColumns = Collections.newSetFromMap(
          new ConcurrentHashMap<String, Boolean>());

  public String getProcessRate() {
    return processRate;
  }

  public void setProcessRate(String processRate) {
    this.processRate = processRate;
  }

  public String getTableUtilState() {
    return tableUtilState;
  }

  public void setTableUtilState(String tableUtilState) {
    this.tableUtilState = tableUtilState;
  }

  public Set<String> getTablesWithBlobClobColumns() {
    return tablesWithBlobClobColumns;
  }
}
