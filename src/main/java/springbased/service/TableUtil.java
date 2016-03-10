package springbased.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class TableUtil {

  private static final Logger log = Logger.getLogger(TableUtil.class);

  public static Map<String, List<String>> columnMap = new HashMap<String, List<String>>();

  public static void fetchDDLAndCopyData(Connection targetConn,
      String targetSchema, Connection sourceConn, String sourceSchema, List<String> tableList) {
    try {
      PreparedStatement pstmt = sourceConn.prepareStatement(
          "select table_name,TEMPORARY from dba_tables dt where upper(owner)=?  and not exists "
              + "(select 1 from dba_tab_columns dtc where upper(dtc.owner)=? and dtc.table_name=dt.table_name and dtc.data_type in ('ROWID','UROWID')) order by table_name");
      ResultSet rs = null;
      pstmt.setString(1, sourceSchema.toUpperCase());
      pstmt.setString(2, sourceSchema.toUpperCase());

      HashMap<String, String> tempTableList = new HashMap<String, String>();

      rs = pstmt.executeQuery();
      while (rs.next()) {
        tableList.add(rs.getString(1));
        tempTableList.put(rs.getString(1), rs.getString(2));
      }
      rs.close();
      pstmt.close();
      copyDataToTable(targetConn, targetSchema, sourceConn, sourceSchema,
          tableList, tempTableList);
    } catch (SQLException e) {
    }
  }

  public static String timeString() {
    return "[" + new Timestamp(System.currentTimeMillis()).toString() + "] ";
  }

  private static void copyDataToTable(Connection targetConn,
      String targetSchema, Connection sourceConn, String sourceSchema,
      List<String> tableList, HashMap<String, String> tempTableList) {
    // get primary key info
    try {
      PreparedStatement pstmt = sourceConn.prepareStatement(
          "select dcc.table_name,dcc.column_name,dcc.constraint_name from dba_cons_columns dcc, dba_constraints dc where upper(dc.owner)=? and upper(dcc.owner)=? and dcc.constraint_name=dc.constraint_name "
              + "and dc.constraint_type='P' order by dcc.table_name,position ");
      pstmt.setString(1, sourceSchema.toUpperCase());
      pstmt.setString(2, sourceSchema.toUpperCase());
      ResultSet rs = pstmt.executeQuery();
      HashMap<String, List<String>> pkmap = new HashMap<String, List<String>>();
      HashMap<String, String> pktable2namemap = new HashMap<String, String>();
      while (rs.next()) {
        String table_name = rs.getString(1);
        String column_name = rs.getString(2);
        String constraint_name = rs.getString(3);
        if (!pkmap.containsKey(table_name)) {
          pkmap.put(table_name, new ArrayList<String>());
          pktable2namemap.put(table_name, constraint_name);
        }
        List<String> cols = pkmap.get(table_name);
        cols.add(column_name);
      }
      rs.close();
      pstmt.close();

      List<String> tableDDLList = new ArrayList<String>();
      List<String> pkDDLList = new ArrayList<String>();
      Map<String, Map<String, Integer>> scaleMap = new HashMap<String, Map<String, Integer>>();
      for (int i = 0; i < tableList.size(); i++) {
        String tableName = tableList.get(i);
        // construct table DDL
        Map<String, Integer> columnMap = new HashMap<String, Integer>();
        String tableDDL = constructTableDDL(tableName, sourceSchema, sourceConn,
            targetSchema, tempTableList, columnMap);
        // add primary key DDL
        String pkDDL = addPKDDL(tableName, targetSchema, pkmap,
            pktable2namemap);
        scaleMap.put(tableName, columnMap);
        tableDDLList.add(tableDDL);
        if (pkDDL != null) {
          pkDDLList.add(pkDDL);
        }
      }

      for (int i = 0; i < tableDDLList.size(); i++) {
        PreparedStatement ps = null;
        try {
          String tableDDL = tableDDLList.get(i);
          // create table in HANA
          ps = targetConn.prepareStatement(tableDDL);
          ps.execute();

        } catch (SQLException e) {
          log.error(e);
        } finally {
          ps.close();
        }
      }

      for (int i = 0; i < pkDDLList.size(); i++) {
        PreparedStatement ps = null;
        try {
          String pkDDL = pkDDLList.get(i);
          // create pk in HANA
          ps = targetConn.prepareStatement(pkDDL);
          ps.execute();
        } catch (SQLException e) {
          log.error(e);
        } finally {
          ps.close();
        }
      }

      // for (String tableName : tableList) {
      // disablePersistentMerge(tableName, targetSchema, targetConn);
      // }
      for (int i = 0; i < tableList.size(); i++) {
        String tableName = tableList.get(i);
        // migrate table data from oracle to HANA.
        migrateTableData(tableName, sourceSchema, sourceConn, targetSchema,
            targetConn, scaleMap.get(tableName));
      }
      // for (String tableName : tableList) {
      // enablePersistentMerge(tableName, targetSchema, targetConn);
      // }
    } catch (IOException e) {
      log.error(e);
    } catch (Exception e) {
      log.error(e);
    }

  }

  public static String constructTableDDL(String tableName, String sourceSchema,
      Connection sourceConn, String targetSchema,
      HashMap<String, String> tempTableList, Map<String, Integer> columnMap)
          throws IOException {
    PreparedStatement pstmt;
    StringBuffer sb = new StringBuffer();
    String ddl = null;
    try {
      String temporary = tempTableList.get(tableName);
      boolean isTempTable = false;
      String globalTemp = "";
      if (temporary != null && temporary.equals("Y")) {
        isTempTable = true;
        globalTemp = " GLOBAL TEMPORARY ";
      }

      sb.append("create " + globalTemp + " table " + targetSchema + "."
          + tableName + " (");
      pstmt = sourceConn.prepareStatement(
          "select column_name , data_type,NULLABLE,char_used, char_length,data_length,"
              + "data_precision,data_scale from dba_tab_columns where upper(owner)=? and upper(table_name)=? "
              + "order by column_id");
      pstmt.setString(1, sourceSchema.toUpperCase());
      pstmt.setString(2, tableName.toUpperCase());
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        String colname = rs.getString(1);
        String datatype = rs.getString(2);
        String nullable = rs.getString(3);
        String lengthType = rs.getString(4);
        Integer datalength = null;
        if (rs.wasNull()) {
          datalength = rs.getInt(6);
        } else {
          datalength = lengthType.equals("C") ? rs.getInt(5) : rs.getInt(6);
        }
        if (rs.wasNull()) {
          datalength = null;
        }
        Integer dataprecision = rs.getInt(7);
        if (rs.wasNull()) {
          dataprecision = null;
        }
        Integer datascale = rs.getInt(8);
        if (rs.wasNull()) {
          datascale = null;
        } else {
          columnMap.put(colname, new Integer(datascale));
        }
        // transform
        if (datatype.equals("VARCHAR2")) {
          datatype = "VARCHAR2(" + datalength.intValue() + ")";
        } else if (datatype.equals("NVARCHAR2")) {
          datatype = "NVARCHAR2(" + datalength.intValue() + ")";
        } else if (datatype.equals("CHAR")) {
          datatype = "VARCHAR(" + datalength.intValue() + ")";
        } else if (datatype.equals("RAW")) {
          datatype = "RAW(" + datalength.intValue() + ")";
        }
        sb.append("\"" + colname + "\" " + datatype + ""
            + (nullable.equals("N") ? " NOT NULL" : "") + " ,");
      }
      rs.close();
      pstmt.close();

      ddl = sb.toString();
      ddl = ddl.substring(0, ddl.length() - 1);
      ddl += ")";

      // Define the temp table behavior
      if (isTempTable) {
        ddl += "ON COMMIT DELETE ROWS";
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ddl;

  }

  public static String addPKDDL(String tableName, String targetSchema,
      HashMap<String, List<String>> pkmap,
      HashMap<String, String> pktable2namemap) throws IOException {
    StringBuffer sb = new StringBuffer();
    sb.append(
        "alter table " + targetSchema + "." + tableName + " add constraint ");

    String colListString = "";
    // fetch everything here
    String pkName = pktable2namemap.get(tableName);
    if (pkName == null || "".equals(pkName)) {
      return null;
    }
    List<String> cols = pkmap.get(tableName);
    if (cols == null || cols.isEmpty()) {
      return null;
    }
    if (pkName.equals(tableName)) {
      pkName = "PK_" + pkName;
    }
    sb.append(pkName + " primary key (");
    if (cols != null) {
      for (int i = 0; i < cols.size(); i++) {
        String column_name = cols.get(i);
        colListString += column_name + ",";
      }
    }

    if (colListString != null) {
      colListString = colListString.substring(0, colListString.length() - 1);
      colListString += ")";
    }
    sb.append(colListString);

    return sb.toString();
  }

  public static void migrateTableData(String tableName, String sourceSchema,
      Connection sourceConn, String targetSchema, Connection targetConn,
      Map<String, Integer> columnScales) {

    String queryString = "SELECT * FROM " + sourceSchema + "." + tableName + "";
    PreparedStatement pstmt;
    try {
      pstmt = sourceConn.prepareStatement(queryString);

      ResultSet queryResult = pstmt.executeQuery();
      ResultSetMetaData md = queryResult.getMetaData();

      String columns = "";
      for (int j = 0; j < md.getColumnCount(); j++) {
        columns = columns + "?,";
      }
      columns = columns.substring(0, columns.length() - 1);

      String insertString = "INSERT INTO " + targetSchema + "." + tableName
          + " VALUES (" + columns + ")";
      PreparedStatement prepStmnt = targetConn.prepareStatement(insertString);

      int cols = md.getColumnCount();
      int rows = 0;

      while (queryResult.next()) {

        for (int i = 1; i <= cols; i++) {
          int type = md.getColumnType(i);
          if (type == Types.DECIMAL || type == Types.DOUBLE
              || type == Types.NUMERIC) {

            if (type == Types.NUMERIC) {
              BigDecimal d = queryResult.getBigDecimal(i);
              if (queryResult.wasNull()) {
                prepStmnt.setNull(i, type);
              } else {
                int scale = 0;
                if (columnScales.get(md.getColumnName(i)) != null) {
                  scale = columnScales.get(md.getColumnName(i)).intValue();
                }
                if (d.precision() > 34
                    && !tableName.contains("EMAIL_TEMPLATE")) {
                  prepStmnt.setBigDecimal(i, new BigDecimal(0));
                } else if (md.getPrecision(i) == 126) {
                  prepStmnt.setFloat(i, d.floatValue());
                } else {
                  prepStmnt.setBigDecimal(i, d);
                }

              }
            } else {
              double d = queryResult.getDouble(i);
              if (queryResult.wasNull()) {
                prepStmnt.setNull(i, type);
              } else {
                prepStmnt.setDouble(i, d);
              }
            }

          } else if (type == Types.INTEGER) {
            int n = queryResult.getInt(i);
            if (queryResult.wasNull()) {
              prepStmnt.setNull(i, type);
            } else {
              prepStmnt.setInt(i, n);
            }

          } else if (type == Types.SMALLINT) {
            short n = queryResult.getShort(i);
            if (queryResult.wasNull()) {
              prepStmnt.setNull(i, type);
            } else {
              prepStmnt.setShort(i, n);
            }

          } else if (type == Types.VARCHAR) {
            String s = queryResult.getString(i);
            prepStmnt.setString(i, s);
          } else if (type == Types.NVARCHAR || type == Types.CHAR) {
            String s = queryResult.getString(i);
            prepStmnt.setString(i, s);
          } else if (type == Types.DATE) {
            Date day = queryResult.getDate(i);
            prepStmnt.setDate(i, day);
          } else if (type == Types.TIME) {
            Time time = queryResult.getTime(i);
            prepStmnt.setTime(i, time);
          } else if (type == Types.TIMESTAMP) {
            Timestamp timestamp = queryResult.getTimestamp(i);

            prepStmnt.setTimestamp(i, timestamp);
            if (timestamp != null) {
              Calendar cal = Calendar.getInstance();
              cal.setTimeInMillis(timestamp.getTime());
              int realyear = cal.get(Calendar.YEAR);

              if (realyear == 0 || realyear < -4713 || realyear > 9999) {
                prepStmnt.setTimestamp(i, null);
              }
            }

          } else if (type == Types.CLOB) {
            String nclob = queryResult.getString(i);
            prepStmnt.setString(i, nclob);

          } else if (type == Types.BLOB) {
            byte[] s = queryResult.getBytes(i);
            prepStmnt.setBytes(i, s);
          } else if (type == Types.VARBINARY || type == Types.BINARY) {
            byte[] s = queryResult.getBytes(i);
            prepStmnt.setBytes(i, s);
          } else {
            byte[] s = queryResult.getBytes(i);
            prepStmnt.setBytes(i, s);
          }
        }
        rows++;
        prepStmnt.addBatch();
        int logSize = 0;
        int batchSize = 100;
        if (batchSize > 0) {
          if (batchSize < 500) {
            logSize = 500;
          } else {
            logSize = batchSize;
          }
          try {
            if (rows % logSize == 0) {
              prepStmnt.executeBatch();
              targetConn.commit();
              prepStmnt.clearBatch();
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

      }

      prepStmnt.executeBatch();
      targetConn.commit();
      prepStmnt.clearBatch();
      queryResult.close();
      pstmt.close();
      prepStmnt.close();
      columns = null;
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
