package springbased.example.dao.impl;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springbased.example.bean.Course;
import springbased.example.dao.CourseDao;

import java.util.List;

@Repository("hibernateContextualSessionCourseDAO")
public class HibernateContextualSessionCourseDAO implements CourseDao {

	public HibernateContextualSessionCourseDAO() {
	}

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public void store(Course course) {
		sessionFactory.getCurrentSession().saveOrUpdate(course);
	}

	@Transactional
	public void delete(Long courseId) {

		Course course = (Course) sessionFactory.getCurrentSession().get(Course.class, courseId);

		sessionFactory.getCurrentSession().delete(course);
	}

	@Transactional(readOnly = true)
	public Course findById(Long courseId) {
		return (Course) sessionFactory.getCurrentSession().get(Course.class, courseId);
	}

	@Transactional(readOnly = true)
	public List<Course> findAll() {

		Query query = sessionFactory.getCurrentSession().createQuery("from Course");

		return query.list();
	}
}
