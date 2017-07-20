package springbased.config;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

import java.io.IOException;

@Configuration
public class SpringMongoConfig {

  public static final String DB = "mongospringbased";

  public static final String bindIp = "localhost";

  public static final int port = 12345;

  public @Bean MongoClient mongoClient() throws Exception {
    MongoClient mongo = new MongoClient(bindIp, port);
    return mongo;
  }

  public @Bean MongoTemplate mongoTemplate() throws Exception {
    MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient(), DB));
    return mongoTemplate;

  }

  @Bean(initMethod = "start", destroyMethod = "stop")
  public MongodExecutable mongodExecutable() throws IOException {
    MongodStarter starter = MongodStarter.getDefaultInstance();
    IMongodConfig mongodConfig = new MongodConfigBuilder()
        .version(Version.Main.PRODUCTION)
        .net(new Net(bindIp, port, Network.localhostIsIPv6()))
        .build();
    MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
    return mongodExecutable;
  }

}