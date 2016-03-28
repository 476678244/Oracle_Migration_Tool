package springbased.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import springbased.bean.ConnectionInfo;
import springbased.dao.AbstractDAO;

@Repository
public class ConnectionInfoDAO extends AbstractDAO<ConnectionInfo> {

  @Override
  protected Class<ConnectionInfo> getClazz() {
    return ConnectionInfo.class;
  }
  
  public List<ConnectionInfo> getAll() {
    return this.basicDAO.find().asList();
  }
  
  public void save(ConnectionInfo connectionInfo) {
    this.basicDAO.save(connectionInfo);
  }

}
