package org.timetravellersmap.core;

import com.google.gson.annotations.Expose;
import org.geotools.map.FeatureLayer;
import org.timetravellersmap.gui.eventpane.AddModifyEventDialog;

import java.util.Calendar;
import java.util.Date;

/**
 * Basemap: representation of a historic map, along with its period of validity
 */
public class Basemap {
    @Expose
    private String filePath;
    @Expose
    private String mapName;
    @Expose
    private int validStartDate;
    @Expose
    private int validEndDate;
//    private org.geotools.map.FeatureLayer layer;

    public Basemap(String filePath, String mapName, int validStartDate, int validEndDate) {
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

    public int getValidStartDate() {
        return validStartDate;
    }

    public int getValidEndDate() {
        return validEndDate;
    }

    public Calendar getValidStartDateAsDate() {
        return AddModifyEventDialog.yearToCalendar(validStartDate);
    }

    public Calendar getValidEndDateAsDate() {
        return AddModifyEventDialog.yearToCalendar(validEndDate);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Basemap) {
            Basemap basemapOther = (Basemap)other;
            return this.mapName.equals(basemapOther.getMapName())
                    && this.filePath.equals(basemapOther.getFilePath())
                    && this.validStartDate == basemapOther.getValidStartDate()
                    && this.validEndDate == basemapOther.getValidEndDate();
        }
        else {
            return other.equals(this);
        }
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = result + 17 * filePath.hashCode();
        result = result + 17 * mapName.hashCode();
        result = result + 17 * validStartDate;
        result = result + 17 * validEndDate;
        return result;
    }

//    public FeatureLayer getLayer() {
//        return layer;
//    }
}
