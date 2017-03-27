package org.timetravellersmap.core;

import org.geotools.map.FeatureLayer;

import java.util.Date;

/**
 * Created by joshua on 27/03/17.
 */
public class Basemap {
    private String filePath;
    private String mapName;
    private Date validStartDate;
    private Date validEndDate;
//    private org.geotools.map.FeatureLayer layer;

    public Basemap(String filePath, String mapName, Date validStartDate, Date validEndDate) {
        this.mapName = mapName;
        this.filePath = filePath;
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

    public String getFilePath() {
        return filePath;
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
