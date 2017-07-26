package springbased.example.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import springbased.example.bean.Vehicle;

import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
@WebAppConfiguration
public class VehicleDaoTest {

	@Autowired
	private VehicleDao vehicleDao;

	@Test
	public void test() {
		Vehicle vehicle = new Vehicle("TEM0001", "Red", 4, 4);
		vehicleDao.insert(vehicle);

		vehicle = vehicleDao.findByVehicleNo("TEM0001");
		System.out.println("Vehicle No: " + vehicle.getVehicleNo());
		System.out.println("Color: " + vehicle.getColor());
		System.out.println("Wheel: " + vehicle.getWheel());
		System.out.println("Seat: " + vehicle.getSeat());
	}

	@Test
	public void testException() {
		Vehicle vehicle = new Vehicle("TEM0002", "Red", 4, 4);
		vehicleDao.insert2(vehicle);
		try {
			vehicleDao.insert2(vehicle);
		} catch (DataAccessException e) {
			SQLException sqle = (SQLException) e.getCause();
			System.out.println("Error code: " + sqle.getErrorCode());
			System.out.println("SQL state: " + sqle.getSQLState());
		}
	}

	@Configuration
	@ComponentScan("springbased.*")
	public static class TestConfiguration {
	}

}
