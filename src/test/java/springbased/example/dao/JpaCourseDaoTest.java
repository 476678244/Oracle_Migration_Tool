package springbased.example.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import springbased.example.bean.Course;
import springbased.example.dao.impl.JpaCourseDao;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
@WebAppConfiguration
public class JpaCourseDaoTest {

	@Autowired
	private CourseDao courseDao;

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
		this.courseDao.store(course);
	}

	@Configuration
	@ComponentScan("springbased.*")
	public static class TestConfiguration {
	}
}
