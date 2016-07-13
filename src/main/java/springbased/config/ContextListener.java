package springbased.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import springbased.service.connectionpool.DataSourceFactory;
import springbased.service.taskpool.MigrationThreadPool;

import java.sql.SQLException;

@WebListener
public class ContextListener implements ServletContextListener {

  private static final Logger log = Logger.getLogger(ContextListener.class);
  
  @Override
  public void contextInitialized(ServletContextEvent event) {
    log.info("The application started");
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    MigrationThreadPool.shutdown();
    try {
      DataSourceFactory.destroyDataSources();
    } catch (SQLException e) {
      log.error("destroy data sources failed!");
    }
    log.info("The application stopped");
  }
}