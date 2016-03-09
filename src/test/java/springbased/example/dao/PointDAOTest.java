package springbased.example.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbased.example.bean.Point;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PointDAOTest {

//	@Autowired
//	private PointDAO<Point> pointDAO;

	@Test
	public void test() {
//		Point p = new Point();
//		p.setX(1);
//		p.setY(1);
//		p.setName("Point1-1");
//		this.pointDAO.getBasicDAO().save(p);
//		int points = this.pointDAO.getBasicDAO().find().asList().size();
//		System.out.println("Points size:" + points);
	}

	@Configuration
	@ComponentScan("springbased.*")
	public static class TestConfiguration {
	}
}