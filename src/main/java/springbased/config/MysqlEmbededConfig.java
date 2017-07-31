package springbased.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.config.Charset.UTF8;

/**
 * https://github.com/wix/wix-embedded-mysql
 */
@Configuration
public class MysqlEmbededConfig {

	static final String MYSQL_USERNAME = "user";

	static final String MYSQL_PASSWORD = "1234";

	static final int MYSQL_PORT = 3306;

	@Bean(destroyMethod = "stop")
	public EmbeddedMysql embeddedMysqlStarter() {
		MysqldConfig config = aMysqldConfig(v5_7_latest)
			.withCharset(UTF8)
			.withPort(MYSQL_PORT)
			.withUser(MYSQL_USERNAME, MYSQL_PASSWORD)
			.withTimeZone("Europe/Vilnius")
			.withTimeout(2, TimeUnit.MINUTES)
			.withServerVariable("max_connect_errors", 666)
			.build();
		EmbeddedMysql.Builder builder = anEmbeddedMysql(config)
			.addSchema("vehicle", classPathScript("db/001_init.sql"));
		return builder.start();
	}

	@Bean
	public DataSource dataSource() throws IOException, SQLException, PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/vehicle");
		dataSource.setUser(MYSQL_USERNAME);
		dataSource.setPassword(MYSQL_PASSWORD);
		dataSource.setInitialPoolSize(2);
		dataSource.setMaxPoolSize(5);
		return dataSource;
	}
}
