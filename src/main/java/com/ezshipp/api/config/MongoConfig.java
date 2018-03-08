package com.ezshipp.api.config;

import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.context.annotation.Profile;

/**
 * Created by srinivasseri on 2/6/18.
 */
@Profile("default")
@Configuration
@EnableMongoRepositories(basePackages = "com.ezshipp.api.repositories")
public class MongoConfig extends AbstractMongoConfiguration {
    private final List<DefaultMongoTypeMapper> converters = new ArrayList<>();

    @Override
    protected String getDatabaseName() {
        return "ezshipp";
    }

//    @Override
//    public Mongo mongo() throws Exception {
//        return new MongoClient("127.0.0.1", 27017);
//    }

    @Value("${mongo.url}")
    private String url;

    @Value("${mongo.db}")
    private String databaseName;


    @Override
    public Mongo mongo() throws Exception {
        return new Mongo(new MongoURI(url));
    }


    @Override
    public CustomConversions customConversions() {
        //converters.add(new OrderWriterConverter());
        converters.add(new DefaultMongoTypeMapper(null));
        return new CustomConversions(converters);
    }

}
