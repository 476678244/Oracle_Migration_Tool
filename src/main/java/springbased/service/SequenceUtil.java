package springbased.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import springbased.bean.ConnectionInfo;
import springbased.monitor.ThreadLocalErrorMonitor;
import springbased.readonly.ReadOnlyConnection;

public class SequenceUtil {

  private static final Logger log = Logger.getLogger(SequenceUtil.class);

  public static void copySequence(ConnectionInfo targetConnInfo,
      String targetSchema, ConnectionInfo sourceConnInfo, String sourceSchema,
      List<String> tableList) throws SQLException {
    // migrate sequence
    ResultSet rs = null;
    PreparedStatement pstmt = null;
    ReadOnlyConnection sourceConn = MigrationService
        .getReadOnlyConnection(sourceConnInfo);
    List<String> seqDDLList = new ArrayList<String>();
    try {
      pstmt = sourceConn.prepareStatement(
          "select sequence_name,MIN_VALUE,INCREMENT_BY,CYCLE_FLAG,LAST_NUMBER, ORDER_FLAG "
              + " from dba_sequences where upper(sequence_owner)=? order by sequence_name");
      try {
        pstmt.setString(1, sourceSchema.toUpperCase());
        pstmt.setFetchSize(2000);
        rs = pstmt.executeQuery();
        while (rs.next()) {
          String seqName = rs.getString(1);
          int minVal = rs.getInt(2);
          int incBy = rs.getInt(3);
          String cycleFlag = rs.getString(4);
          BigDecimal lastNum = rs.getBigDecimal(5);
          String orderFlag = rs.getString(6);
          String seqDDL = "CREATE SEQUENCE  " + targetSchema + "." + seqName
              + " INCREMENT BY " + incBy + " START WITH " + lastNum + " "
              + (cycleFlag.equals("Y") ? " CYCLE " : " NOCYCLE ")
              + (orderFlag.equals("Y") ? " ORDER " : " NOORDER ");
          seqDDLList.add(seqDDL);
          if (ThreadLocalErrorMonitor.isDebugMode()) {
            log.info("constructed create sequence DDL:" + seqDDL);
          }
        }
      } catch (SQLException e) {
        log.error(e);
      }
    } finally {
      rs.close();
      pstmt.close();
      sourceConn.close();
    }

    for (int i = 0; i < seqDDLList.size(); i++) {
      String seqDDL = null;
      Connection targetConn = MigrationService.getConnection(targetConnInfo);
      try {
        seqDDL = seqDDLList.get(i);
        // System.out.println(seqDDL);
        pstmt = targetConn.prepareStatement(seqDDL);
        pstmt.execute();
        log.info("successfully run:" + seqDDL);
      } catch (SQLException e) {
        log.error(e);
        ThreadLocalErrorMonitor.add(seqDDL, e);
      } finally {
        pstmt.close();
        targetConn.close();
      }
    }
  }
}
