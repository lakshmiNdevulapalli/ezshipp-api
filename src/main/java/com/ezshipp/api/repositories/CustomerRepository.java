package com.ezshipp.api.repositories;

import com.ezshipp.api.document.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by srinivasseri on 2/4/18.
 */

public interface CustomerRepository extends MongoRepository<Customer, String> {

    /**
     * <p>findById.</p>
     *
     * @param id a {@link String} object.
     * @return a {@link Optional} object.
     */
    Optional<Customer> findById(String id);
}
