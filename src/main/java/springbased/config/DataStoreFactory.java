package springbased.config;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import springbased.bean.CopyTableDataRequest;

@Repository
public class DataStoreFactory {

    private static Datastore datastore;

    @PostConstruct
    void init() throws UnknownHostException {
        datastore = new Morphia().mapPackage("springbased.bean").createDatastore(
                new MongoClient(), SpringMongoConfig.DB);
    }

    @Bean
    public Datastore datastore() {
        return datastore;
    }

    private static final String HOSTNAME = "127.0.0.1";
    private static final int PORT = 27017;

    private static Datastore ds = null;

    public static Datastore getCopyTableDataRequestDS() {
        if (ds == null) {
            try {
                ds = new Morphia().map(CopyTableDataRequest.class).createDatastore(
                        new MongoClient(HOSTNAME), "copydatarequest");
            } catch (UnknownHostException e) {
                return null;
            }
        }
        return ds;
    }
}
