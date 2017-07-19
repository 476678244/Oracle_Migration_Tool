package springbased.config;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import de.flapdoodle.embed.mongo.MongodExecutable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;

@Repository
public class DataStoreFactory {

  private static Datastore datastore;

  @Autowired
  private MongoClient mongoClient;

  @Autowired
  private MongodExecutable mongodExecutable;

  @PostConstruct
  void init() throws IOException {
    this.mongodExecutable.start();
    datastore = new Morphia().mapPackage("springbased.bean").createDatastore(
        mongoClient, SpringMongoConfig.DB);
  }

  @Bean
  public Datastore datastore() {
    return datastore;
  }

  @PreDestroy
  void destroy() {
    this.mongodExecutable.stop();
  }
}
