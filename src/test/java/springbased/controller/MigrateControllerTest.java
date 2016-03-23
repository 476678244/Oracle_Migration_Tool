package springbased.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MigrateControllerTest {

  @Autowired
  private MigrateController controller;

  @Test
  public void test() {
    String sourceUsername = "sfuser";
    String sourcePassword = "sfuser";
    String sourceUrl = "jdbc:oracle:thin:@10.58.100.66:1521:dbpool1";
    String sourceSchema = "sfuser_tree";
    String targetUsername = "sfuser";
    String targetPassword = "sfuser";
    String targetUrl = "jdbc:oracle:thin:@10.58.100.66:1521:dbpool1";
    String targetSchema = "sfuser_real";
    boolean test = false;
    if (test) {
      this.controller.migrate(sourceUsername, sourcePassword, sourceUrl,
          sourceSchema, targetUsername, targetPassword, targetUrl,
          targetSchema);
    }
  }

  @Configuration
  @ComponentScan("springbased.*")
  public static class TestConfiguration {
  }
}
