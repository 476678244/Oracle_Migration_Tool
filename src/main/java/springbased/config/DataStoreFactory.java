package springbased.config;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

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

  @PostConstruct
  void init() throws UnknownHostException {
    datastore = new Morphia().mapPackage("springbased.bean").createDatastore(
        mongoClient, SpringMongoConfig.DB);
  }

  @Bean
  public Datastore datastore() {
    return datastore;
  }
}
