package springbased.service;

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

public class FKUtil {

  private static final Logger log = Logger.getLogger(FKUtil.class);

  public static void addFK(String sourceSchema, String targetSchema,
      ConnectionInfo sourceConnInfo, ConnectionInfo targetConnInfo)
          throws SQLException {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ReadOnlyConnection sourceConn = MigrationService
        .getReadOnlyConnection(sourceConnInfo);
    List<String> sqls = new ArrayList<String>();
    try {

      pstmt = sourceConn.prepareStatement(
          "Select 'alter table ' || Owner || '.' || Table_Name || ' add constraint ' || Constraint_Name "
              + "|| ' foreign key ' "
              + "|| ' (' || ( Select Listagg(Column_Name,',') Within Group (Order By Position) Column_List "
              + "From All_Cons_Columns Acc Where upper(Owner) = ? and Owner = Ac.Owner And Acc.Constraint_Name = Ac.Constraint_Name "
              + "And Acc.Owner = Ac.Owner "
              + "Group By Constraint_Name) || ') references '|| Owner || '.' || "
              + "(Select Table_Name From All_Constraints Ac2 Where Ac2.Constraint_Name = Ac.R_Constraint_Name And upper(Owner) = ?) "
              + "|| ' (' || ( Select Listagg(Column_Name,',') Within Group (Order By Position) Column_List "
              + "From All_Cons_Columns Acc Where upper(Owner) = ? and Owner = Ac.Owner And Acc.Constraint_Name = Ac.R_Constraint_Name  "
              + "and acc.owner = ac.owner "
              + "Group By Constraint_Name) || ')  ' "
              + "Fk_Statement, DELETE_RULE From All_Constraints Ac Where Constraint_Type = 'R' And upper(Owner) = ?");
      pstmt.setString(1, sourceSchema.toUpperCase());
      pstmt.setString(2, sourceSchema.toUpperCase());
      pstmt.setString(3, sourceSchema.toUpperCase());
      pstmt.setString(4, sourceSchema.toUpperCase());
      log.info("adding FK...");
      rs = pstmt.executeQuery();
      String sql = null;
      while (rs.next()) {
        sql = rs.getString(1).toUpperCase()
            .replaceAll(sourceSchema.toUpperCase(), targetSchema.toUpperCase());
        String deleteRule = rs.getString(2);
        if (!"NO ACTION".equals(deleteRule)) {
          sql += " ON DELETE CASCADE";
        }
        log.info("constructed add FK DDL:" + sql);
        sqls.add(sql);
        sql = null;
      }
    } catch (SQLException e) {
      log.error(e);
    } finally {
      rs.close();
      pstmt.close();
      sourceConn.close();
    }

    for (String sql : sqls) {
      PreparedStatement pstmtTarget = null;
      Connection targetConn = MigrationService.getConnection(targetConnInfo);
      try {
        pstmtTarget = targetConn.prepareStatement(sql);
        pstmtTarget.executeUpdate();
        log.info("successfully run:" + sql);
      } catch (SQLException e) {
        log.error(e);
        ThreadLocalErrorMonitor.add(sql, e);
      } finally {
        if (pstmtTarget != null) {
          pstmtTarget.close();
        }
        targetConn.close();
      }
    }
  }
}
