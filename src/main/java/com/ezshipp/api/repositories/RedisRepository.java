package com.ezshipp.api.repositories;

import com.ezshipp.api.document.Driver;

import java.util.Map;

public interface RedisRepository {

    /**
     * Return all movies
     */
    Map<Object, Object> findAllDrivers();

    /**
     * Add key-value pair to Redis.
     */
    void add(Driver driver);

    /**
     * Delete a key-value pair in Redis.
     */
    void delete(String id);
    
    /**
     * find a movie
     */
    Driver findDriver(String id);
    
}
