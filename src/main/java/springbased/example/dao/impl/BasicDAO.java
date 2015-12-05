package springbased.example.dao.impl;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

public class BasicDAO<T> extends org.mongodb.morphia.dao.BasicDAO<T, ObjectId> {

	BasicDAO(Class<T> clazz, Datastore ds) {
		super(clazz, ds);
	}

}
