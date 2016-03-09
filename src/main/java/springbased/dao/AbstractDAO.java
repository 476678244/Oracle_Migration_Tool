package springbased.dao;

import javax.annotation.PostConstruct;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDAO<T> {

  @Autowired
  protected Datastore datastore;

  protected BasicDAO<T, ObjectId> basicDAO;

  // return T.class
  protected abstract Class<T> getClazz();

  @PostConstruct
  public final void init() {
    this.basicDAO = new BasicDAO<T, ObjectId>(getClazz(), this.datastore);
  }
}
