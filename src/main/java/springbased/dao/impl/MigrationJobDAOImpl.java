package springbased.dao.impl;

import org.springframework.stereotype.Repository;

import springbased.bean.MigrationJob;
import springbased.dao.AbstractDAO;
import springbased.dao.MigrationJobDAO;

@Repository
public class MigrationJobDAOImpl extends AbstractDAO<MigrationJob>
    implements MigrationJobDAO {

  @Override
  protected Class<MigrationJob> getClazz() {
    return MigrationJob.class;
  }

}
