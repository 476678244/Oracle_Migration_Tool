package springbased.example.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import springbased.example.bean.Vehicle;
import springbased.example.dao.VehicleDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcVehicleDao implements VehicleDao {

	public JdbcVehicleDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void insert(Vehicle vehicle) {
		String sql = "INSERT INTO VEHICLE (VEHICLE_NO, COLOR, WHEEL, SEAT) " + "VALUES (?, ?, ?, ?)";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, vehicle.getVehicleNo());
			ps.setString(2, vehicle.getColor());
			ps.setInt(3, vehicle.getWheel());
			ps.setInt(4, vehicle.getSeat());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public void insert2(final Vehicle vehicle) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				String sql = "INSERT INTO VEHICLE " + "(VEHICLE_NO, COLOR, WHEEL, SEAT) " + "VALUES (?, ?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, vehicle.getVehicleNo());
				ps.setString(2, vehicle.getColor());
				ps.setInt(3, vehicle.getWheel());
				ps.setInt(4, vehicle.getSeat());
				return ps;
			}
		});
	}

	public void insert3(final Vehicle vehicle) {
		String sql = "INSERT INTO VEHICLE (VEHICLE_NO, COLOR, WHEEL, SEAT) VALUES (?, ?, ?, ?)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, vehicle.getVehicleNo());
				ps.setString(2, vehicle.getColor());
				ps.setInt(3, vehicle.getWheel());
				ps.setInt(4, vehicle.getSeat());
			}
		});
	}

	public void insert4(final Vehicle vehicle) {
		String sql = "INSERT INTO VEHICLE (VEHICLE_NO, COLOR, WHEEL, SEAT) VALUES (?, ?, ?, ?)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sql, vehicle.getVehicleNo(),vehicle.getColor(),vehicle.getWheel(), vehicle.getSeat() );
	}

	public Vehicle findByVehicleNo(String vehicleNo) {
		String sql = "SELECT * FROM VEHICLE WHERE VEHICLE_NO = ?";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, vehicleNo);
			Vehicle vehicle = null;
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				vehicle = new Vehicle(rs.getString("VEHICLE_NO"), rs.getString("COLOR"), rs.getInt("WHEEL"), rs.getInt("SEAT"));
			}
			rs.close();
			ps.close();
			return vehicle;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public void update(Vehicle vehicle) {/* ... */}

	public void delete(Vehicle vehicle) {/* ... */}
}
