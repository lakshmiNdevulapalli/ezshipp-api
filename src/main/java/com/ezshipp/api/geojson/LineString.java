package com.ezshipp.api.geojson;

import java.util.Arrays;
import java.util.Collection;

/**
 * A MultiPoint is a geometry consisting of multiple points.
 */
public class LineString extends MultiPoint {
    public LineString() {
    }

    public LineString(Collection<? extends Point> c) {
        super(c);
    }

    public LineString(Point... points) {
        this(Arrays.asList(points));
    }

    LineString(Object object) {
        super(object);
    }

    @Override
    public String getType() {
        return "LineString";
    }
}
