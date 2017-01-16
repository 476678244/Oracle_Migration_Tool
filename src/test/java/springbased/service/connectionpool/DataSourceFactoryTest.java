package springbased.service.connectionpool;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import springbased.bean.ConnectionInfo;

public class DataSourceFactoryTest {

	@Test
	public void test() {
		String ip = "127.0.0.1";
		String sid = "XE";
		String username = "SYS";
		String password = "welcome";
//		String login_role = "sysoper";
		String url = "jdbc:oracle:thin:@" + ip + ":1521:" + sid;
		System.out.println(url);
//		url = url + ";internal_logon=" + login_role;
//		System.out.println(url);
	    ConnectionInfo sourceConInfo = new ConnectionInfo(username, password, url);
	    sourceConInfo.setLogin_role("");
		DataSource ds = DataSourceFactory.getDataSource(sourceConInfo);
		System.out.println("accquired datasource:"+ds);
		try {
			Connection connection = ds.getConnection();
			System.out.println("acquired connection:" + connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
