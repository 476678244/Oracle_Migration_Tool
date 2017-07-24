package springbased.config;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.MysqldConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.distribution.Version.v5_7_latest;
import static com.wix.mysql.config.MysqldConfig.aMysqldConfig;
import static com.wix.mysql.config.Charset.UTF8;

@Configuration
public class MysqlEmbededConfig {

	@Bean(destroyMethod = "stop")
	public EmbeddedMysql embeddedMysqlBuilder() {
		MysqldConfig config = aMysqldConfig(v5_7_latest)
			.withCharset(UTF8)
			.withPort(3306)
			.withUser("user", "1234")
			.withTimeZone("Europe/Vilnius")
			.withTimeout(2, TimeUnit.MINUTES)
			.withServerVariable("max_connect_errors", 666)
			.build();
		EmbeddedMysql.Builder builder = anEmbeddedMysql(config)
			.addSchema("vehicle", classPathScript("db/001_init.sql"));
		return builder.start();
	}

	@Bean
	public DataSource dataSource() throws ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/vehicle");
		dataSource.setUsername("user");
		dataSource.setPassword("1234");
		dataSource.setInitialSize(2);
		dataSource.setMaxTotal(5);
		return dataSource;
	}
}
