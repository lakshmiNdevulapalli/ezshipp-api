package com.ezshipp.api.repositories;

import com.ezshipp.api.document.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by srinivasseri on 2/4/18.
 */

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
