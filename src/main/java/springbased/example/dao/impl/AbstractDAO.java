package springbased.example.dao.impl;

import javax.annotation.PostConstruct;

import org.mongodb.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDAO<T> {

	@Autowired
	protected Datastore datastore;

	protected BasicDAO<T> basicDAO;

	public BasicDAO<T> getBasicDAO() {
		return basicDAO;
	}

	// return T.class
	protected abstract Class<T> getClazz();

	@PostConstruct
	public final void init() {
		this.basicDAO = new BasicDAO<T>(getClazz(), this.datastore);
	}
}
