package springbased.dao.impl;

import org.springframework.stereotype.Repository;

import springbased.bean.ConnectionInfo;
import springbased.dao.AbstractDAO;
import springbased.dao.ConnectionInfoDAO;

@Repository
public class ConnectionInfoDAOimpl extends AbstractDAO<ConnectionInfo>
    implements ConnectionInfoDAO {

  @Override
  protected Class<ConnectionInfo> getClazz() {
    return ConnectionInfo.class;
  }

}
