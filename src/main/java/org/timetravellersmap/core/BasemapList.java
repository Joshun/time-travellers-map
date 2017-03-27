package org.timetravellersmap.core;

import org.hamcrest.BaseMatcher;

import java.util.*;

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
        basemapDateMap.get(startDate).get(endDate).remove(basemap);
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

    private ArrayList<Basemap> getFlattenedBasemaps() {
        ArrayList<Basemap> flattened = new ArrayList<>();
        Collection<HashMap<Date, ArrayList<Basemap>>> flat1 = basemapDateMap.values();
        for (HashMap<Date, ArrayList<Basemap>> flat2: flat1) {
            Collection<ArrayList<Basemap>> flat3 = flat2.values();
            for (ArrayList<Basemap> flat4: flat3) {
                flattened.addAll(flat4);
            }
        }
        return flattened;
    }

    public Object[][] generateTableRows() {
        ArrayList<Basemap> flattenedBasemaps = getFlattenedBasemaps();
        Object[][] tableRows = new Object[flattenedBasemaps.size()][];
        for (int i=0; i<tableRows.length; i++) {
            Basemap basemap = flattenedBasemaps.get(i);
            tableRows[i] = new Object[3];
            tableRows[i][0] = basemap.getName();
            Calendar start = new GregorianCalendar();
            start.setTime(basemap.getValidStartDate());
            Calendar end = new GregorianCalendar();
            end.setTime(basemap.getValidEndDate());
            tableRows[i][1] = start.get(Calendar.YEAR);
            tableRows[i][2] = end.get(Calendar.YEAR);
        }
        return tableRows;
    }
}
