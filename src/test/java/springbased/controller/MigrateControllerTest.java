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
    String sourceUrl = "jdbc:oracle:thin:@192.168.248.135:1521:dbpool1";
    String sourceSchema = "sfuser_yyhrbp";
    String targetUsername = "sfuser";
    String targetPassword = "sfuser";
    String targetUrl = "jdbc:oracle:thin:@192.168.248.135:1521:dbpool1";
    String targetSchema = "sfuser_jdm";
    String ip = "192.168.248.135";
    String sid = "dbpool1";
    long cost = 0;
    boolean test = false;
    if (test) {
      long start = System.currentTimeMillis(), end =0;
      ThreadLocalMonitor.setInfo(new Info());
      this.controller.migrate(sourceUsername, sourcePassword, sourceUrl,
          sourceSchema, targetUsername, targetPassword, targetUrl,
          targetSchema);
      end = System.currentTimeMillis();
      cost = end - start;
    }
    long get = ThreadLocalMonitor.getBytesTime.longValue();
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
