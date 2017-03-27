package org.timetravellersmap.core;

import org.hamcrest.BaseMatcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by joshua on 27/03/17.
 */
public class BasemapList {
    private HashMap<Date,HashMap<Date, ArrayList<Basemap>>> basemapDateMap = new HashMap<>();

    public void addBasemap(Basemap basemap) {
        Date startDate = basemap.getValidStartDate();
        Date endDate = basemap.getValidEndDate();

        if (!basemapDateMap.containsKey(startDate)) {
           basemapDateMap.put(startDate, new HashMap<>());
        }
        if (! basemapDateMap.get(startDate).containsKey(endDate)) {
            basemapDateMap.get(startDate).put(endDate, new ArrayList<>());
        }
        basemapDateMap.get(startDate).get(endDate).add(basemap);
    }

    public void removeBasemap(Basemap basemap) {
        basemapDateMap.get(basemap.getValidStartDate()).get(basemap.getValidEndDate()).remove(basemap);
    }

    public ArrayList<Basemap> getValidBasemaps(Date validStartDate, Date validEndDate) {
        return basemapDateMap.get(validStartDate).get(validEndDate);
    }

    public Basemap getValidBasemap(Date validStartDate, Date validEndDate) {
        return getValidBasemaps(validStartDate, validEndDate).get(0);
    }
}
