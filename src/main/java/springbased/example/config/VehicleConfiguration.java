package springbased.example.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbased.example.dao.VehicleDao;
import springbased.example.dao.impl.JdbcVehicleDao;

import javax.sql.DataSource;

@Configuration
public class VehicleConfiguration {

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public VehicleDao vehicleDao() throws ClassNotFoundException {
		return new JdbcVehicleDao(dataSource);
	}
}
