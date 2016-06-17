package springbased.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import springbased.bean.ConnectionInfo;
import springbased.bean.MigrationProcess;
import springbased.bean.ValidationResult;
import springbased.readonly.ReadOnlyConnection;
import springbased.service.connectionpool.DataSourceFactory;

@Service
public class MigrationService {

  private static final Logger log = Logger.getLogger(MigrationService.class);

  public void copyTables(MigrationProcess process) {

  }

  public static Connection getConnection(ConnectionInfo connectionInfo)
      throws SQLException, InterruptedException {
    Connection connection = DataSourceFactory.getDataSource(connectionInfo)
        .getConnection();
    if (connection == null) {
      log.error("Connection is null");
      throw new SQLException("Connection is null");
    }
    if (Thread.currentThread().isInterrupted()) {
      throw new InterruptedException("Migration Job is interrupted");
    }
    return connection;
  }

  public static ReadOnlyConnection getReadOnlyConnection(ConnectionInfo connectionInfo)
      throws SQLException, InterruptedException {
    Connection connection = getConnection(connectionInfo);
    if (connection == null) {
      log.error("Connection is null");
      throw new SQLException("Connection is null");
    }
    connection.setReadOnly(true);
    return new ReadOnlyConnection(connection);
  }

  public ValidationResult validateSourceSchema(ReadOnlyConnection connection,
      String schema) throws SQLException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    // check have that schema user
    try {
      ps = connection.prepareStatement(
          "select username from DBA_USERS where username = ?");
      ps.setString(1, schema.toUpperCase());
      rs = ps.executeQuery();
      if (!rs.next()) {
        return new ValidationResult() {
          @Override
          public int getStatus() {
            return FAIL;
          }

          @Override
          public String getCause() {
            return "No source schema is found in source DB!";
          }
        };
      }
    } finally {
      rs.close();
      ps.close();
    }
    log.info("validate for " + schema + " success!");
    return new ValidationResult();
  }

  public ValidationResult validateTargetSchema(Connection connection,
      String schema) throws SQLException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    // check have that schema user
    try {
      ps = connection.prepareStatement(
          "select username from DBA_USERS where username = ?");
      ps.setString(1, schema.toUpperCase());
      rs = ps.executeQuery();
      if (!rs.next()) {
        return new ValidationResult() {
          @Override
          public int getStatus() {
            return FAIL;
          }

          @Override
          public String getCause() {
            return "No target schema is found in target DB!";
          }
        };
      }
    } finally {
      rs.close();
      ps.close();
    }
    try {
      ps = connection.prepareStatement(
          "select table_name from dba_tables where owner = ?");
      ps.setString(1, schema.toUpperCase());
      rs = ps.executeQuery();
      if (rs.next()) {
        return new ValidationResult() {
          @Override
          public int getStatus() {
            return FAIL;
          }

          @Override
          public String getCause() {
            return "Tables exit in target schema, please recreate this schema at first!";
          }
        };
      }
    } finally {
      rs.close();
      ps.close();
    }
    log.info("validate for " + schema + " success!");
    return new ValidationResult();
  }
  
  public void recreateSchema(Connection connection,
      String schema) throws SQLException {
    if (!schema.contains("_"))
       return ;
    PreparedStatement ps = null;
    try {
      ps = connection.prepareStatement(
          " drop user " + schema +" cascade");
      ps.execute();
    } finally {
      ps.close();
    }
    try {
      ps = connection.prepareStatement(
          " CREATE USER " + schema + " IDENTIFIED BY sfuser "
              + " DEFAULT TABLESPACE DATA_01 " + " TEMPORARY TABLESPACE TEMP "
              + " QUOTA UNLIMITED ON DATA_01 " + " QUOTA UNLIMITED ON INDEX_01 "
              + " QUOTA UNLIMITED ON LOB_01 " + " QUOTA UNLIMITED ON MVIEW_01 ");
      ps.execute();
    } finally {
      ps.close();
    }
  }

}
