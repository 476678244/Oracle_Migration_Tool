package springbased.example.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;
import springbased.example.bean.Course;
import springbased.example.dao.CourseDao;

import java.util.List;

public class HibernateCourseDao implements CourseDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public HibernateCourseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void store(Course course) {
        Session session = this.sessionFactory.getCurrentSession();
        session.save(course);
    }

    @Transactional
    @Override
    public void delete(Long courseId) {
        this.sessionFactory.openSession().delete(courseId);
    }

    @Override
    public Course findById(Long courseId) {
        return null;
    }

    @Override
    public List<Course> findAll() {
        return null;
    }
}
