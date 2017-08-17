package springbased.example.dao;

import com.wix.mysql.EmbeddedMysql;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import springbased.example.bean.Course;
import springbased.example.dao.impl.HibernateCourseDao;
import springbased.example.dao.impl.JpaCourseDao;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
@WebAppConfiguration
public class JpaCourseDaoTest {

	@Autowired
	private CourseIncFeeDao courseIncFeeDao;

	@Autowired
	private CourseDao hibernateContextualSessionCourseDAO;

	@Test
	public void test() {
		CourseDao courseDao = new JpaCourseDao();
		Course course = new Course();
		course.setTitle("course1");
		course.setBeginDate(new Date());
		course.setEndDate(new Date());
		course.setFee(100);
		courseDao.store(course);
		courseDao.store(course);
		List<Course> courses = courseDao.findAll();
		courses.forEach(c -> System.out.println(c.getId()));
		int a = 0;
	}

	@Test
	public void testHibernate() {
		Course course = new Course();
		course.setTitle("course1");
		course.setBeginDate(new Date());
		course.setEndDate(new Date());
		course.setFee(100);
		this.courseIncFeeDao.store(course);
	}

	@Test
	public void testPropagation() {
		Course course = new Course();
		course.setTitle("course2");
		course.setBeginDate(new Date());
		course.setEndDate(new Date());
		course.setFee(100);
		this.courseIncFeeDao.store(course);
		Course course5 = this.courseIncFeeDao.findById(course.getId());
		try {
			courseIncFeeDao.incFee20(course5);
		} catch (RuntimeException re) {
			int a = 0;
		}
		this.courseIncFeeDao.findAll();
	}

	@Test
	public void testContextualSession() {
		Course course = new Course();
		course.setTitle("course1");
		course.setBeginDate(new Date());
		course.setEndDate(new Date());
		course.setFee(100);
		this.hibernateContextualSessionCourseDAO.store(course);
		Course c = this.hibernateContextualSessionCourseDAO.findById(course.getId());
		this.hibernateContextualSessionCourseDAO.delete(c.getId());
	}

	@Configuration
	@ComponentScan("springbased.*")
	public static class TestConfiguration {
	}
}
