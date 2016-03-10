package springbased.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springbased.bean.ConnectionInfo;
import springbased.bean.MigrationProcess;
import springbased.dao.impl.ConnectionInfoDAO;
import springbased.dao.impl.MigrationJobDAO;

@Service
public class MigrationService {

  private static final Logger log = Logger.getLogger(MigrationService.class);
  
  @Autowired
  private ConnectionInfoDAO connectionInfoDAO;
  
  @Autowired
  private MigrationJobDAO migrationJobDAO;
  
  public void copyTables(MigrationProcess process) {
    
  }
  
  public Connection getConnection(ConnectionInfo connectionInfo) {
    try {
      Connection connection = DriverManager.getConnection(connectionInfo.getUrl(),
          connectionInfo.getUsername(), connectionInfo.getPassword());
      return connection;
    } catch (SQLException e) {
      log.error(e);
      return null;
    }
  }
  
}
