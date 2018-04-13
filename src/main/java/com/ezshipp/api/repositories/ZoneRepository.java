package com.ezshipp.api.repositories;

import com.ezshipp.api.document.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ZoneRepository extends MongoRepository<Zone, String> {

//    List<Zone> findByLocationWithin(Polygon polygon);
//    List<Zone> findByLocationWithinPoint(Point point);
}
