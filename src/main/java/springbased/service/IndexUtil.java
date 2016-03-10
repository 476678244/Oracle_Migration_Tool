package springbased.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class IndexUtil {

  private static final Logger log = Logger.getLogger(IndexUtil.class);
  
  public static void copyIndex(Connection targetConn, String targetSchema,
      Connection sourceConn, String sourceSchema, List<String> tableList) {
    // copy unique index
    try {
      PreparedStatement pstmt;
      pstmt = sourceConn.prepareStatement(
          " select table_name,index_name,COLUMN_NAME from " + "("
              + "select dic.table_name,dic.index_name,dic.COLUMN_NAME ,dic.COLUMN_POSITION from dba_indexes  di, "
              + "dba_ind_columns dic where upper(di.owner)=? and di.uniqueness='UNIQUE' and "
              + "di.owner=dic.index_owner and di.index_name=dic.index_name "
              + "and not exists (select 1 from dba_constraints dc where dc.owner=di.owner and dc.CONSTRAINT_NAME=di.index_name and dc.CONSTRAINT_TYPE='P') "
              + "and index_type='NORMAL' ) order by table_name,index_name, COLUMN_POSITION");
      pstmt.setString(1, sourceSchema.toUpperCase());
      // pstmt.setString(2, sourceSchema);
      ResultSet rs = pstmt.executeQuery();
      List<String> indexList = new ArrayList<String>();
      HashMap<String, String> tab2Ind = new HashMap<String, String>();
      HashMap<String, List<String>> cols2Ind = new HashMap<String, List<String>>();
      while (rs.next()) {
        String tabName = rs.getString(1);
        String indName = rs.getString(2);
        String colName = rs.getString(3);
        tab2Ind.put(indName, tabName);
        log.info("table vs index found:" + tabName + " vs " + indName);
        List<String> colList = cols2Ind.get(indName);
        if (colList == null) {
          colList = new ArrayList<String>();
          cols2Ind.put(indName, colList);
        }
        colList.add(colName);
      }
      rs.close();
      pstmt.close();
      pstmt = sourceConn.prepareStatement(
          " select table_name,index_name,COLUMN_NAME from " + "("
              + "select dic.table_name,dic.index_name,dic.COLUMN_EXPRESSION as column_name ,dic.COLUMN_POSITION  from dba_indexes  di,"
              + "DBA_IND_EXPRESSIONS dic where upper(di.owner)=? and di.uniqueness='UNIQUE' and "
              + "di.owner=dic.index_owner and di.index_name=dic.index_name  "
              + "and not exists (select 1 from dba_constraints dc where dc.owner=di.owner and dc.CONSTRAINT_NAME=di.index_name and dc.CONSTRAINT_TYPE='P') "
              + "and index_type='FUNCTION-BASED NORMAL' and 1=0"
              + ") order by table_name,index_name, COLUMN_POSITION");
      pstmt.setString(1, sourceSchema.toUpperCase());
      rs = pstmt.executeQuery();
      while (rs.next()) {
        String tabName = rs.getString(1);
        String indName = rs.getString(2);
        String colName = rs.getString(3);
        tab2Ind.put(indName, tabName);
        log.info("table vs index found:" + tabName + " vs " + indName);
        List<String> colList = cols2Ind.get(indName);
        if (colList == null) {
          colList = new ArrayList<String>();
          cols2Ind.put(indName, colList);
        }
        colList.add(colName);

      }
      rs.close();
      pstmt.close();
      Iterator iter = tab2Ind.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry entry = (Map.Entry) iter.next();
        String indName = (String) entry.getKey();
        String tabName = (String) entry.getValue();
        List<String> colList = cols2Ind.get(indName);
        String colString = "";
        for (int i = 0; i < colList.size(); i++) {
          String colName = colList.get(i);
          colString += colName + ",";
        }
        colString = colString.substring(0, colString.length() - 1);
        String indexDDL = "create unique index " + targetSchema + "." + indName
            + " on " + targetSchema + "." + tabName + " (" + colString + ")";
        if (tableList.contains(tabName)) {
          log.info("constructed create index DDL :" + indexDDL);
          indexList.add(indexDDL);
        }
      }

      for (int i = 0; i < indexList.size(); i++) {
        try {
          String indexDDL = indexList.get(i);
          pstmt = targetConn.prepareStatement(indexDDL);
          pstmt.execute();
          log.info("successfully run:" + indexDDL);
        } catch (SQLException e) {
          log.error(e);
        } finally {
          pstmt.close();
        }
      }
    } catch (SQLException e) {
      log.error(e);
      if (e.getErrorCode() != 261) {
      }
    }
  }

}
