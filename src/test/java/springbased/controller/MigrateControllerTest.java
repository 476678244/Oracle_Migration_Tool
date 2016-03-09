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
    String sourceUrl = "";
    String sourceSchema = "sfuser_tree";
    this.controller.migrate(sourceUsername, sourcePassword, sourceUrl,
        sourceSchema, null, null, null, null);
  }

  @Configuration
  @ComponentScan("springbased.*")
  public static class TestConfiguration {
  }
}
