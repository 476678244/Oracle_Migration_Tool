package springbased.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import springbased.service.taskpool.MigrationThreadPool;

@WebListener
public class ContextListener implements ServletContextListener {

  private static final Logger log = Logger.getLogger(ContextListener.class);

  @Autowired
  private MigrationThreadPool migrationThreadPool;
  
  @Override
  public void contextInitialized(ServletContextEvent event) {
    log.info("The application started");
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    this.migrationThreadPool.shutdown();
    log.info("The application stopped");
  }
}