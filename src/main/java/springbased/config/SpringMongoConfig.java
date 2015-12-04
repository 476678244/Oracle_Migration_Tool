package springbased.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;

@Configuration
public class SpringMongoConfig {

  public static final String DB = "mongospringbased";

  public @Bean MongoDbFactory mongoDbFactory() throws Exception {
    return new SimpleMongoDbFactory(new MongoClient(), DB);
  }

  public @Bean MongoTemplate mongoTemplate() throws Exception {
    MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
    return mongoTemplate;

  }

}