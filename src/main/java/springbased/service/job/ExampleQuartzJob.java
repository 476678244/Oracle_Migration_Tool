package springbased.service.job;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExampleQuartzJob {

  @Scheduled(fixedRate = 2000)
  public void scheduledRun() {
    System.out.println("Time:" + new Date());
  }
}
