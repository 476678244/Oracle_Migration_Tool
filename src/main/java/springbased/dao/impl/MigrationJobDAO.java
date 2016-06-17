package springbased.dao.impl;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import springbased.bean.MigrationJob;
import springbased.bean.StatusEnum;
import springbased.dao.AbstractDAO;

@Repository
public class MigrationJobDAO extends AbstractDAO<MigrationJob> {

  private static final Logger log = Logger.getLogger(MigrationJobDAO.class);
  @Override
  protected Class<MigrationJob> getClazz() {
    return MigrationJob.class;
  }
  
  public Collection<MigrationJob> getAll() {
    return this.basicDAO.find().asList();
  }
  
  public void save(MigrationJob job) {
    this.basicDAO.save(job);
  }
  
  public void updateStatus(long jobId, StatusEnum statusEnum) {
    this.basicDAO.update(this.basicDAO.createQuery().filter("jobId", jobId),
        this.basicDAO.createUpdateOperations().set("status", statusEnum));
    log.info("job " + jobId + "is updates status to " + statusEnum);
  }
  
  public void updateStartTime(long jobId, Date startTime) {
    this.basicDAO.update(this.basicDAO.createQuery().filter("jobId", jobId),
        this.basicDAO.createUpdateOperations().set("startTime", startTime));
  }

  public void updateEndTime(long jobId, Date endTime) {
    this.basicDAO.update(this.basicDAO.createQuery().filter("jobId", jobId),
        this.basicDAO.createUpdateOperations().set("endTime", endTime));
  }
}
