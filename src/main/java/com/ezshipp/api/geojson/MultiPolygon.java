package com.ezshipp.api.geojson;

import java.util.Arrays;
import java.util.Collection;

/**
 * A MultiPolygon is a geometry consisting of multiple polygons.
 */
public class MultiPolygon extends AbstractGeoJsonObject<Polygon> {
    @Override
    public String getType() {
        return "MultiPolygon";
    }

    public MultiPolygon() {
    }

    public MultiPolygon(Collection<? extends Polygon> c) {
        super(c);
    }

    public MultiPolygon(Polygon... polygons) {
        this(Arrays.asList(polygons));
    }

    MultiPolygon(Object object) {
        super(object);
    }

    @Override
    public void validate(PointValidator pointValidator) {
        if(size() == 0) throw new IllegalStateException("A MultiPolygon must contain at least 1 polygon");
        for (Polygon polygon : this) {
            polygon.validate(pointValidator);
        }
    }
}
