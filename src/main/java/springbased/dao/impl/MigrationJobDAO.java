package springbased.dao.impl;

import org.springframework.stereotype.Repository;

import springbased.bean.MigrationJob;
import springbased.dao.AbstractDAO;

@Repository
public class MigrationJobDAO extends AbstractDAO<MigrationJob> {

  @Override
  protected Class<MigrationJob> getClazz() {
    return MigrationJob.class;
  }

}
