package com.ezshipp.api.service;

import com.ezshipp.api.document.Driver;
import com.ezshipp.api.repositories.DriverRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by srinivasseri on 2/16/18.
 */
@Service
public class DriverService {

    @Inject
    private MongoTemplate mongoTemplate;

    @Inject
    private DriverRepository driverRepository;

    @Cacheable(value="driversCache")
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Cacheable(value="Driver", key = "#driverId")
    public Driver findByDriverId(String driverId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(driverId));
        return mongoTemplate.findOne(query, Driver.class);
    }
}
