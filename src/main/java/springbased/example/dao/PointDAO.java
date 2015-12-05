package springbased.example.dao;

import springbased.example.dao.impl.BasicDAO;

public interface PointDAO<T> {

	BasicDAO<T> getBasicDAO();
}
