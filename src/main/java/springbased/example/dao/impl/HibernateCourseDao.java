package springbased.example.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import springbased.example.bean.Course;
import springbased.example.dao.CourseDao;
import springbased.example.dao.CourseIncFeeDao;

import java.util.Date;
import java.util.List;

public class HibernateCourseDao implements CourseIncFeeDao {
    private HibernateCourseDaoSupport hibernateCourseDaoSupport;

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public HibernateCourseDao(SessionFactory sessionFactory, HibernateCourseDaoSupport hibernateCourseDaoSupport) {
        this.sessionFactory = sessionFactory;
        this.hibernateCourseDaoSupport = hibernateCourseDaoSupport;
    }

    @Transactional
    @Override
    public void store(Course course) {
        Session session = this.sessionFactory.getCurrentSession();
        session.save(course);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void incFee20(Course course) {
        TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
        Course course1 = this.findById(course.getId());
        course1.setFee(course1.getFee() + 10);
        this.sessionFactory.getCurrentSession().save(course1);
        try {
            /**
             * 分析了一下，原因是A方法（有事务）调用B方法（要独立新事务），如果两个方法写在同一个类里，spring的事务会只处理能同一个。
             * 解决方案：需要将两个方法分别写在不同的类里。
             */
            this.hibernateCourseDaoSupport.newCourse();
            // as incFee30 will join current transaction, it will see fee 100
            this.hibernateCourseDaoSupport.incFee30(course);
            // as incFee10 will start new transaction, it will not see fee as 110 , but see as 100
            this.hibernateCourseDaoSupport.incFee10(course1);
        } catch (RuntimeException re) {
            this.findAll();
        }
    }

    @Transactional
    @Override
    public void delete(Long courseId) {
        this.sessionFactory.openSession().delete(courseId);
    }

    @Transactional
    @Override
    public Course findById(Long courseId) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.find(Course.class, courseId);
    }

    @Transactional
    @Override
    public List<Course> findAll() {
        return this.sessionFactory.getCurrentSession().createCriteria(Course.class).list();
    }

    public List<Course> dbSearchAll() {
        return this.sessionFactory.openSession().createCriteria(Course.class).list();
    }
}
