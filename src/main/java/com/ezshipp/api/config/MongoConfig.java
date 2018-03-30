package com.ezshipp.api.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srinivasseri on 2/6/18.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.ezshipp.api.repositories")
public class MongoConfig extends AbstractMongoConfiguration {
    private final List<DefaultMongoTypeMapper> converters = new ArrayList<>();

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    private @Value("${spring.data.mongodb.uri}") String uri;

    private @Value("${spring.data.mongodb.db}") String databaseName;


    @Override
    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI(uri));
    }

}
