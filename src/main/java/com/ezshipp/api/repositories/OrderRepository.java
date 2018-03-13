package com.ezshipp.api.repositories;

import com.ezshipp.api.document.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Created by srinivasseri on 2/3/18.
 */
public interface OrderRepository extends MongoRepository<Order, String> {
    /**
     * <p>findByTitle.</p>
     *
     * @param orderId a {@link String} object.
     * @return a {@link Order} object.
     */
    @Query("{ 'orderseqId' : { $regex: ?0 } }")
    Order findByOrderId(String regexp);

    /**
     * <p>findById.</p>
     *
     * @param id a {@link String} object.
     * @return a {@link Optional} object.
     */
    Optional<Order> findById(String id);

    @Query(value = "{'Date':{ $lt: ?0, $gt: ?1}}")
    //@Query(value = "{ "dob" : { "$lte" : { "$date" : "2015-05-16T07:55:23.257Z"}}}
    List<Order> findByDateBetween(Instant from, Instant to);
}
