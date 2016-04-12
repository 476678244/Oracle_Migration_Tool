package springbased.service.connectionpool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import springbased.bean.ConnectionInfo;

public class DataSourceFactory {

  private static Map<ConnectionInfo, DataSource> dataSources = new 
      ConcurrentHashMap<ConnectionInfo, DataSource>();
  
  public static DataSource getDataSource(ConnectionInfo connectionInfo) {
    if (dataSources.containsKey(connectionInfo)) {
      return dataSources.get(connectionInfo);
    } else {
      BasicDataSource ds = new BasicDataSource();   
      ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
      ds.setUsername(connectionInfo.getUsername());
      ds.setPassword(connectionInfo.getPassword());
      ds.setUrl(connectionInfo.getUrl());
      ds.setMinIdle(0);
      ds.setMaxIdle(1);
      ds.setInitialSize(0);
      ds.setMaxTotal(1);
      ds.setMaxOpenPreparedStatements(100);
      dataSources.put(connectionInfo, ds);
      return ds;
    }
  }
}
