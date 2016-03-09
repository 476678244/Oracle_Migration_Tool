package springbased.dao.impl;

import org.springframework.stereotype.Repository;

import springbased.bean.ConnectionInfo;
import springbased.dao.AbstractDAO;

@Repository
public class ConnectionInfoDAO extends AbstractDAO<ConnectionInfo> {

  @Override
  protected Class<ConnectionInfo> getClazz() {
    return ConnectionInfo.class;
  }

}
