package com.ezshipp.api.repositories;

import com.ezshipp.api.document.Counters;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CounterRepository extends MongoRepository<Counters, String> {
}
