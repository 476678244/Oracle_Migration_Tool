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
      ds.setMaxIdle(5);
      ds.setInitialSize(1);
      ds.setMaxTotal(20);
      ds.setMaxOpenPreparedStatements(200);
      dataSources.put(connectionInfo, ds);
      return ds;
    }
  }
}