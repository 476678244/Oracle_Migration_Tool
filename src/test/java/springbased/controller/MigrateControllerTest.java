package springbased.controller;

import java.sql.SQLException;

import de.flapdoodle.embed.mongo.MongodExecutable;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StringUtils;

import springbased.monitor.Info;
import springbased.monitor.ThreadLocalMonitor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
@WebAppConfiguration
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
      validateInput("DATA_MODEL,_custom09_read;");
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
  
  private static boolean validateInput(String inputString) {
    if (StringUtils.isEmpty(inputString)) {
      return false;
    }

    String[] permStrArray = inputString.split(";");

    if (!validate(permStrArray)) {
      return false;
    }
    for (String permStr : permStrArray) {
      String[] permAttributes = permStr.split(",");
      if (!validate(permAttributes)) {
        return false;
      }

      if (!(permAttributes.length ==2 || permAttributes.length == 3)) {
        return false;
      }

      String permStringValue = permAttributes[0];
      if (permStringValue.startsWith("_")) {
        permStringValue = add$ForPermissionValueString(permStringValue);
      }
      String permType = permAttributes[1];
      String permLongValue = null;
      if(permAttributes.length == 3){
        permLongValue = permAttributes[2];
      }

      if (StringUtils.isEmpty(permStringValue)) {
        return false;
      }
      if (StringUtils.isEmpty(permType)) {
        return false;
      }

    }

    return true;
  }
  
  private static <T> boolean validate(T[] values) {
    if (values == null || values.length == 0) {
      return false;
    }
    return true;
  }
  
  public static String add$ForPermissionValueString(String _fieldIdFieldActionString) {
    return "$" + _fieldIdFieldActionString;
  }
 

  @Configuration
  @ComponentScan("springbased.*")
  public static class TestConfiguration {
  }
}
