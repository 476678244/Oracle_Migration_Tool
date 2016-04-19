package springbased.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import springbased.bean.ConnectionInfo;
import springbased.monitor.ThreadLocalErrorMonitor;
import springbased.monitor.ThreadLocalMonitor;
import springbased.readonly.ReadOnlyConnection;

public class UKUtil {
  
  private static final Logger log = Logger.getLogger(UKUtil.class);

  public static void execute(ConnectionInfo targetConnInfo,
      String targetSchema, ConnectionInfo sourceConnInfo, String sourceSchema)
          throws SQLException, InterruptedException {
    ThreadLocalMonitor.getInfo().setTableUtilState("collecting UK contrain");
    ReadOnlyConnection sourceConn = MigrationService.getReadOnlyConnection(sourceConnInfo);
    // get Unique key info
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Map<String, List<String>> uk2Columns = new HashMap<String, List<String>>();
    Map<String, String> uk2Table = new HashMap<String, String>();
    Map<Integer, Set<String>> columnCount2Uks = new HashMap<Integer, Set<String>>();
    try {
      pstmt = sourceConn.prepareStatement(
          "select dcc.table_name,dcc.column_name,dcc.constraint_name "
              + " from dba_cons_columns dcc, dba_constraints dc where upper(dc.owner)=? "
              + " and upper(dcc.owner)=? and dcc.constraint_name=dc.constraint_name "
              + "and dc.constraint_type='U' order by dcc.table_name,position ");
      pstmt.setString(1, sourceSchema.toUpperCase());
      pstmt.setString(2, sourceSchema.toUpperCase());
      pstmt.setFetchSize(2000);
      rs = pstmt.executeQuery();

      while (rs.next()) {
        String tableName = rs.getString(1);
        String columnName = rs.getString(2);
        String constraintName = rs.getString(3);
        // initial columns list if need for uk2Columns
        if (!uk2Columns.containsKey(constraintName)) {
          uk2Columns.put(constraintName, new ArrayList<String>());
          uk2Table.put(constraintName, tableName);
        }
        uk2Columns.get(constraintName).add(columnName);
        // initial uk list if need for columnCount2Uk
        int constraintColumnCount = uk2Columns.get(constraintName).size();
        if (columnCount2Uks.get(constraintColumnCount) == null) {
          columnCount2Uks.put(constraintColumnCount, new HashSet<String>());
        }
        if (constraintColumnCount > 1) {
          // remove from former count set 
          columnCount2Uks.get(constraintColumnCount - 1).remove(constraintName);
        }
        // add to new count set
        columnCount2Uks.get(constraintColumnCount).add(constraintName);
        if (ThreadLocalErrorMonitor.isDebugMode()) {
          log.info(sourceSchema + "find constraint_name:" + constraintName);;
        }
      }
    } catch (SQLException e) {
      log.error(e);
    } finally {
      rs.close();
      pstmt.close();
      sourceConn.close();
    }
    
    ThreadLocalMonitor.getInfo().setTableUtilState("adding UK contrain");
    for (int columnCount : columnCount2Uks.keySet()) {
      for (String constraint : columnCount2Uks.get(columnCount)) {
        Connection targetConn = MigrationService.getConnection(targetConnInfo);
        String tableName = uk2Table.get(constraint);
        String sql = getUKDDL(targetSchema, tableName, constraint,
            uk2Columns.get(constraint));
        try {
          pstmt = targetConn.prepareStatement(sql);
          pstmt.execute();
          if (ThreadLocalErrorMonitor.isDebugMode()) {
            log.info("successfully run:" + sql);
          }
        } catch (SQLException e) {
          log.error(e);
          ThreadLocalErrorMonitor.add(sql, e);
        } finally {
          pstmt.close();
          targetConn.close();
        }
      }
    }
    ThreadLocalMonitor.getInfo().setTableUtilState("");
    ThreadLocalMonitor.setUks(uk2Table.keySet());
  }
  
  private static String getUKDDL(String targetSchema, String tableName,
      String constraintName, List<String> cols) {
    StringBuffer sb = new StringBuffer();
    sb.append(
        "alter table " + targetSchema + "." + tableName + " add constraint ");
    String colListString = "";
    sb.append(constraintName + " UNIQUE (");
    for (int i = 0; i < cols.size(); i++) {
      colListString += cols.get(i) + ",";
    }
    colListString = colListString.substring(0, colListString.length() - 1);
    colListString += ")";
    sb.append(colListString);
    return sb.toString();
  }
}
