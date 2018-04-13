package com.ezshipp.api.document;


import com.ezshipp.api.model.PolygonProps;
import com.ezshipp.api.model.Pricing;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by srinivasseri on 4/2/18.
 */
@Data
@Document(collection = "zones")
//@JsonIgnoreProperties(value = {"createdAt"})
public class Zone {

    @Id
    private String id;
    private String city;
    private String title;
    private String city_id;
    private String zoneseq;
    private PolygonProps polygonProps;
    private List<Pricing> pricing;
    private GeoJsonPolygon polygons;

}
