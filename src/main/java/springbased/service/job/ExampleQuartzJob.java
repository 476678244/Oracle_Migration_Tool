package springbased.service.job;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

public class ExampleQuartzJob extends QuartzJobBean {

  @Override
  protected void executeInternal(JobExecutionContext arg0)
      throws JobExecutionException {
    System.out.println("ExampleQuartzJob:" + new Date());

  }
}
