package springbased.service.connectionpool;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import springbased.bean.ConnectionInfo;

public class DataSourceFactory {

  private static Map<ConnectionInfo, DataSource> dataSources = new 
      ConcurrentHashMap<ConnectionInfo, DataSource>();
  
  public static DataSource getDataSource(ConnectionInfo connectionInfo) {
    if (dataSources.containsKey(connectionInfo)) {
      return dataSources.get(connectionInfo);
    } else {
        try {
          ComboPooledDataSource ds = new ComboPooledDataSource();
          ds.setDriverClass("oracle.jdbc.driver.OracleDriver"); //loads the jdbc driver
          ds.setJdbcUrl(connectionInfo.getUrl());
          ds.setUser(connectionInfo.getUsername());
          ds.setPassword(connectionInfo.getPassword());
          ds.setInitialPoolSize(1);
          ds.setMinPoolSize(0);
          ds.setAcquireIncrement(1);
          ds.setMaxPoolSize(6);
          ds.setMaxStatements(200);
          dataSources.put(connectionInfo, ds);
          return ds;
        } catch (PropertyVetoException e) {
          return null;
        }
    }
  }

  public static void destroyDataSources() throws SQLException {
    for (DataSource dataSource : dataSources.values()) {
      DataSources.destroy(dataSource);
    }
  }
}
