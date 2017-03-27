package org.timetravellersmap.core;

import org.geotools.map.FeatureLayer;

import java.util.Date;

/**
 * Created by joshua on 27/03/17.
 */
public class Basemap {
    private String fileName;
    private String mapName;
    private Date validStartDate;
    private Date validEndDate;
//    private org.geotools.map.FeatureLayer layer;

    public Basemap(String fileName, String mapName, Date validStartDate, Date validEndDate) {
        this.mapName = mapName;
        this.fileName = fileName;
        this.validStartDate = validStartDate;
        this.validEndDate = validEndDate;
    }

//    public Basemap(String fileName, String mapName, Date validStartDate, Date validEndDate, FeatureLayer layer) {
//        this.mapName = mapName;
//        this.fileName = fileName;
//        this.validStartDate = validStartDate;
//        this.validEndDate = validEndDate;
//        this.layer = layer;
//    }

    public String getFileName() {
        return fileName;
    }

    public String getMapName() {
        return mapName;
    }

    public Date getValidStartDate() {
        return validStartDate;
    }

    public Date getValidEndDate() {
        return validEndDate;
    }

//    public FeatureLayer getLayer() {
//        return layer;
//    }
}
