package springbased.dao.impl;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import springbased.bean.MigrationJob;
import springbased.bean.StatusEnum;
import springbased.dao.AbstractDAO;

@Repository
public class MigrationJobDAO extends AbstractDAO<MigrationJob> {

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
  }

}
