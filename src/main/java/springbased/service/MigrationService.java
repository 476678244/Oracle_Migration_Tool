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
      throws SQLException {
    Connection connection = DataSourceFactory.getDataSource(connectionInfo)
        .getConnection();
    return connection;
  }

  public static ReadOnlyConnection getReadOnlyConnection(ConnectionInfo connectionInfo)
      throws SQLException {
    Connection connection = getConnection(connectionInfo);
    if (connection == null)
      return null;
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
    return new ValidationResult();
  }

}
