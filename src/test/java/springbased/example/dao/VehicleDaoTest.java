package springbased.example.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import springbased.example.bean.Vehicle;

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

	@Configuration
	@ComponentScan("springbased.*")
	public static class TestConfiguration {
	}

}
