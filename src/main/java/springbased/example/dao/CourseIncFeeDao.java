package springbased.example.dao;

import springbased.example.bean.Course;

public interface CourseIncFeeDao extends CourseDao {

	void incFee20(Course course);
}
