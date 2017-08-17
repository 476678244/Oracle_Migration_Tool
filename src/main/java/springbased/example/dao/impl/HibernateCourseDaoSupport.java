package springbased.example.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import springbased.example.bean.Course;

import java.util.Date;

public class HibernateCourseDaoSupport {

	private SessionFactory sessionFactory;

	public HibernateCourseDaoSupport(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void incFee10(Course course) {
		TransactionStatus status = TransactionAspectSupport.currentTransactionStatus();
		Session session = this.sessionFactory.getCurrentSession();
		Course course1 = session.find(Course.class, course.getId());
		course1.setFee(course1.getFee() + 10);
		session.save(course1);
		throw new RuntimeException("roll back parent transaction");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void newCourse() {
		Session session = this.sessionFactory.getCurrentSession();
		Course course10 = new Course();
		course10.setTitle("course10");
		course10.setBeginDate(new Date());
		course10.setEndDate(new Date());
		course10.setFee(100);
		session.save(course10);
	}
}
