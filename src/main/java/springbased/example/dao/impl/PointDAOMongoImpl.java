package springbased.example.dao.impl;

import org.springframework.stereotype.Repository;

import springbased.example.bean.Point;
import springbased.example.dao.PointDAO;

@Repository
public class PointDAOMongoImpl extends AbstractDAO<Point> implements PointDAO<Point> {

	@Override
	protected Class<Point> getClazz() {
		return Point.class;
	}

}
