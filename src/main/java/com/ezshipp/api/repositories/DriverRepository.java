package com.ezshipp.api.repositories;

import com.ezshipp.api.document.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by srinivasseri on 2/16/18.
 */
public interface DriverRepository extends MongoRepository<Driver, String> {
}
