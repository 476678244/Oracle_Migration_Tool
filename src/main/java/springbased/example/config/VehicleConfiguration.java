package springbased.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import springbased.example.dao.VehicleDao;
import springbased.example.dao.impl.JdbcVehicleDao;

import javax.sql.DataSource;

@Configuration
public class VehicleConfiguration {

	@Autowired
	private DataSource dataSource;

	@Bean
	public VehicleDao vehicleDao() {
		JdbcVehicleDao dao = new JdbcVehicleDao(dataSource);
		dao.setDataSource(dataSource);
		return dao;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}
}
