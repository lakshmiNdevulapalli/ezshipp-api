package com.ezshipp.api.service;

import com.ezshipp.api.document.Driver;
import com.ezshipp.api.exception.ServiceException;
import com.ezshipp.api.repositories.DriverRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by srinivasseri on 2/16/18.
 */
@Service
public class BikerService {

    @Inject
    private MongoTemplate mongoTemplate;

    @Inject
    private DriverRepository driverRepository;

    @Cacheable(value="driversCache")
    public List<Driver> getAllDrivers() throws ServiceException {
        return driverRepository.findAll();
    }

    @Cacheable(value="driversCache", key = "#driverId")
    public Driver findByDriverId(String driverId) throws ServiceException {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(driverId));
        return mongoTemplate.findOne(query, Driver.class);
    }

    public List<Driver> findDriversByDepoIdAndAccStatus(String depoId, int accStatus) throws ServiceException   {
        Query query = new Query();
        query.addCriteria(where("depoId").is(depoId).andOperator(
                where("acc_status").is(accStatus)));
        return mongoTemplate.find(query, Driver.class,"drivers");
    }

    @Cacheable(value="driversCache", key = "#sessionToken")
    public Driver findByDriverSessionToken(String sessionToken) throws ServiceException {
        Query query = new Query();
        query.addCriteria(Criteria.where("sessionToken").is(sessionToken));
        return mongoTemplate.findOne(query, Driver.class);
    }
}
