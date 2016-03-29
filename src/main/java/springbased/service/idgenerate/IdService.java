package springbased.service.idgenerate;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springbased.bean.MigrationJob;
import springbased.dao.impl.MigrationJobDAO;

@Service
public class IdService {

  private static AtomicLong max = new AtomicLong(0);

  @Autowired
  private MigrationJobDAO jobDAO;

  public long generateNextJobId() {
    if (max.longValue() == 0) {
      synchronized (max) {
        // init max
        Collection<MigrationJob> jobs = this.jobDAO.getAll();
        for (MigrationJob job : jobs) {
          if (job.getJobId() > max.longValue()) {
            max = new AtomicLong(job.getJobId());
          }
        }
      }
    }
    return max.incrementAndGet();
  }
}
