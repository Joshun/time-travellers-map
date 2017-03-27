package org.timetravellersmap.core;

import org.geotools.map.FeatureLayer;

import java.util.Date;

/**
 * Created by joshua on 27/03/17.
 */
public class Basemap {
    private Date validStartDate;
    private Date validEndDate;
    private org.geotools.map.FeatureLayer layer;

    public Basemap(Date validStartDate, Date validEndDate, FeatureLayer layer) {
        this.validStartDate = validStartDate;
        this.validEndDate = validEndDate;
        this.layer = layer;
    }

    public Date getValidStartDate() {
        return validStartDate;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

    public FeatureLayer getLayer() {
        return layer;
    }
}
