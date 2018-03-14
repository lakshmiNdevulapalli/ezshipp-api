package com.ezshipp.api.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
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
        return databaseName;
    }

    @Value("${mongo.uri}")
    private String uri;

    @Value("${mongo.db}")
    private String databaseName;

    @Override
    public MongoClient mongoClient() {
        System.out.println("******************************************URL: " + uri);
        return new MongoClient(new MongoClientURI(uri));
    }

}
