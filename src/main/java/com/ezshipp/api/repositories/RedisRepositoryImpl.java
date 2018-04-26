package com.ezshipp.api.repositories;

import com.ezshipp.api.document.Driver;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private static final String KEY = "DriverStats";
    
    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;
    
    @Inject
    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }
    
    public void add(final Driver driver) {
        hashOperations.put(KEY, driver.get_id(), driver.getName());
    }

    public void delete(final String id) {
        hashOperations.delete(KEY, id);
    }
    
    public Driver findDriver(final String id){
        return (Driver) hashOperations.get(KEY, id);
    }
    
    public Map<Object, Object> findAllDrivers(){
        return hashOperations.entries(KEY);
    }

  
}
