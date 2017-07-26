package springbased.example.dao.impl;

import springbased.example.bean.Course;
import springbased.example.dao.CourseDao;

import javax.persistence.*;
import java.util.List;

public class JpaCourseDao implements CourseDao {

	private EntityManagerFactory entityManagerFactory;

	public JpaCourseDao() {
		entityManagerFactory = Persistence.createEntityManagerFactory("course");
	}

	public void store(Course course) {

		EntityManager manager = entityManagerFactory.createEntityManager();
		EntityTransaction tx = manager.getTransaction();
		try {

			tx.begin();

			manager.merge(course);

			tx.commit();
		} catch (RuntimeException e) {

			tx.rollback();

			throw e;
		} finally {

			manager.close();
		}

	}

	public void delete(Long courseId) {

		EntityManager manager = entityManagerFactory.createEntityManager();
		EntityTransaction tx = manager.getTransaction();
		try {
			tx.begin();
			Course course = manager.find(Course.class, courseId);
			manager.remove(course);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		} finally {
			manager.close();
		}

	}

	public Course findById(Long courseId) {
		EntityManager manager = entityManagerFactory.createEntityManager();
		try {
			return manager.find(Course.class, courseId);
		} finally {
			manager.close();
		}
	}

	public List<Course> findAll() {

		EntityManager manager = entityManagerFactory.createEntityManager();
		try {

			Query query = manager.createQuery("select course from Course course");

			return query.getResultList();
		} finally {

			manager.close();
		}

	}
}
