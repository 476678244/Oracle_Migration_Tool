package springbased.example.dao.impl;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import springbased.example.bean.Vehicle;
import springbased.example.dao.VehicleDao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcVehicleDao extends NamedParameterJdbcDaoSupport implements VehicleDao {

	public JdbcVehicleDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private DataSource dataSource;

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
		jdbcTemplate.update(sql, vehicle.getVehicleNo(), vehicle.getColor(), vehicle.getWheel(), vehicle.getSeat());
	}

	public void insert5(final Vehicle vehicle) {
		String sql = "INSERT INTO VEHICLE (VEHICLE_NO, COLOR, WHEEL, SEAT) " + "VALUES (:vehicleNo, :color, :wheel, :seat)";

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("vehicleNo", vehicle.getVehicleNo());
		parameters.put("color", vehicle.getColor());
		parameters.put("wheel", vehicle.getWheel());
		parameters.put("seat", vehicle.getSeat());
		getNamedParameterJdbcTemplate().update(sql, parameters);
	}

	public void insertBatch(final List<Vehicle> vehicles) {
		String sql = "INSERT INTO VEHICLE (VEHICLE_NO, COLOR, WHEEL, SEAT) VALUES (?, ?, ?, ?)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return vehicles.size();
			}

			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Vehicle vehicle = vehicles.get(i);
				ps.setString(1, vehicle.getVehicleNo());
				ps.setString(2, vehicle.getColor());
				ps.setInt(3, vehicle.getWheel());
				ps.setInt(4, vehicle.getSeat());
			}
		});
	}

	public void insertBatch2(List<Vehicle> vehicles) {
		String sql = "INSERT INTO VEHICLE (VEHICLE_NO, COLOR, WHEEL, SEAT) " + "VALUES (:vehicleNo, :color, :wheel, :seat)";

		List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>();
		for (Vehicle vehicle : vehicles) {
			parameters.add(new BeanPropertySqlParameterSource(vehicle));
		}

		getNamedParameterJdbcTemplate().batchUpdate(sql, parameters.toArray(new SqlParameterSource[parameters.size()]));

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

	public Vehicle findByVehicleNo2(String vehicleNo) {

		String sql = "SELECT * FROM VEHICLE WHERE VEHICLE_NO = ?";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		final Vehicle vehicle = new Vehicle();
		jdbcTemplate.query(sql,

			new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					vehicle.setVehicleNo(rs.getString("VEHICLE_NO"));
					vehicle.setColor(rs.getString("COLOR"));
					vehicle.setWheel(rs.getInt("WHEEL"));
					vehicle.setSeat(rs.getInt("SEAT"));
				}

			}, vehicleNo);
		return vehicle;

	}

	public Vehicle findByVehicleNo3(String vehicleNo) {

		String sql = "SELECT * FROM VEHICLE WHERE VEHICLE_NO = ?";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate.queryForObject(sql, new RowMapper<Vehicle>() {

			public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
				Vehicle vehicle = new Vehicle();
				vehicle.setVehicleNo(rs.getString("VEHICLE_NO"));
				vehicle.setColor(rs.getString("COLOR"));
				vehicle.setWheel(rs.getInt("WHEEL"));
				vehicle.setSeat(rs.getInt("SEAT"));
				return vehicle;
			}

		}, vehicleNo);

	}

	@Override
	public List<Vehicle> findAll() {
		String sql = "SELECT * FROM VEHICLE";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : rows) {
			Vehicle vehicle = new Vehicle();
			vehicle.setVehicleNo((String) row.get("VEHICLE_NO"));
			vehicle.setColor((String) row.get("COLOR"));
			vehicle.setWheel((Integer) row.get("WHEEL"));
			vehicle.setSeat((Integer) row.get("SEAT"));
			vehicles.add(vehicle);
		}
		return vehicles;
	}

	@Override
	public String getColor(String vehicleNo) {

		String sql = "SELECT COLOR FROM VEHICLE WHERE VEHICLE_NO = ?";

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate.queryForObject(sql, String.class, vehicleNo);

	}

	public void update(Vehicle vehicle) {/* ... */}

	public void delete(Vehicle vehicle) {/* ... */}
}
