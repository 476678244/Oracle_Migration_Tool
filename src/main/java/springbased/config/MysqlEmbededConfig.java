package springbased.config;

import com.wix.mysql.EmbeddedMysql;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.ScriptResolver.classPathScript;
import static com.wix.mysql.distribution.Version.v5_7_latest;

@Configuration
public class MysqlEmbededConfig {

	@Bean(destroyMethod = "stop")
	public EmbeddedMysql embeddedMysqlBuilder() {
		EmbeddedMysql.Builder builder = anEmbeddedMysql(v5_7_latest)
			.addSchema("aschema", classPathScript("db/001_init.sql"));
		return builder.start();
	}
}
