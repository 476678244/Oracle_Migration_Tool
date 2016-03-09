package springbased.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import springbased.bean.MigrationProcess;
import springbased.dao.impl.ConnectionInfoDAO;
import springbased.dao.impl.MigrationJobDAO;

@Service
public class MigrationService {

  @Autowired
  private ConnectionInfoDAO connectionInfoDAO;
  
  @Autowired
  private MigrationJobDAO migrationJobDAO;
  
  public void copyTables(MigrationProcess process) {
    
  }
}
