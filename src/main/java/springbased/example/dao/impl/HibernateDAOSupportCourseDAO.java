package springbased.example.dao.impl;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import springbased.example.bean.Course;
import springbased.example.dao.CourseDao;

import java.util.List;

public class HibernateDAOSupportCourseDAO extends HibernateDaoSupport implements CourseDao {

	@Transactional
	public void store(Course course) {
		getHibernateTemplate().saveOrUpdate(course);
	}

	@Transactional
	public void delete(Long courseId) {
		Course course = getHibernateTemplate().get(Course.class, courseId);
		getHibernateTemplate().delete(course);
	}

	@Transactional(readOnly = true)
	public Course findById(Long courseId) {
		return getHibernateTemplate().get(Course.class, courseId);
	}

	@Transactional(readOnly = true)
	public List<Course> findAll() {
		return (List<Course>) getHibernateTemplate().find("from Course");
	}
}
