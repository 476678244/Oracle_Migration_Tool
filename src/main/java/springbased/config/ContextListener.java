package springbased.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import springbased.service.taskpool.MigrationThreadPool;

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
    log.info("The application stopped");
  }
}