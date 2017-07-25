package springbased.example.dao;

import springbased.example.bean.Vehicle;

import java.util.List;

public interface VehicleDao {

	public void insert(Vehicle vehicle);

	public void update(Vehicle vehicle);

	public void delete(Vehicle vehicle);

	public Vehicle findByVehicleNo(String vehicleNo);

	public List<Vehicle> findAll();

	public String getColor(String vehicleNo);
}
