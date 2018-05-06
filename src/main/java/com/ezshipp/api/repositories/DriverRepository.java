package com.ezshipp.api.repositories;

import com.ezshipp.api.document.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * Created by srinivasseri on 2/16/18.
 */
public interface DriverRepository extends MongoRepository<Driver, String> {
    Optional<Driver> findDriverBy_id(String id);

    @Query("{ 'driverseqId' : { $regex: ?0 } }")
    Driver findByDriverseqId(String regexp);
}

    /*@Override
    Optional<Driver> findById(String s);
    */
