package springbased.controller;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbased.monitor.Info;
import springbased.monitor.ThreadLocalMonitor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MigrateControllerTest {

  @Autowired
  private MigrateController controller;

  @Test
  public void test() throws SQLException, InterruptedException {
    String sourceUsername = "sfuser";
    String sourcePassword = "sfuser";
    String sourceUrl = "jdbc:oracle:thin:@10.58.100.66:1521:dbpool1";
    String sourceSchema = "sfuser_tree";
    String targetUsername = "sfuser";
    String targetPassword = "sfuser";
    String targetUrl = "jdbc:oracle:thin:@10.58.100.66:1521:dbpool1";
    String targetSchema = "sfuser_real";
    String ip = "10.58.100.66";
    String sid = "dbpool1";
    boolean test = false;
    if (test) {
      ThreadLocalMonitor.setInfo(new Info());
      this.controller.migrate(sourceUsername, sourcePassword, sourceUrl,
          sourceSchema, targetUsername, targetPassword, targetUrl,
          targetSchema);
    }
    boolean test1 = false;
    if (test1) {
      this.controller.recreateSchema(ip, targetUsername, targetPassword, sid,
          targetSchema);
    }
  }

  @Configuration
  @ComponentScan("springbased.*")
  public static class TestConfiguration {
  }
}
