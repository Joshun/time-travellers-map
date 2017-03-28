package org.timetravellersmap.core;

import com.google.gson.annotations.Expose;
import org.hamcrest.BaseMatcher;
import org.timetravellersmap.gui.eventpane.AddModifyEventDialog;

import java.util.*;

/**
 * Created by joshua on 27/03/17.
 */
public class BasemapList {
    @Expose
    private TreeMap<Integer,TreeMap<Integer, ArrayList<Basemap>>> basemapDateMap = new TreeMap<>();

    public void addBasemap(Basemap basemap) {
        Integer startDate = basemap.getValidStartDate();
        Integer endDate = basemap.getValidEndDate();

        if (!basemapDateMap.containsKey(startDate)) {
           basemapDateMap.put(startDate, new TreeMap<>());
        }
        if (! basemapDateMap.get(startDate).containsKey(endDate)) {
            basemapDateMap.get(startDate).put(endDate, new ArrayList<>());
        }
        basemapDateMap.get(startDate).get(endDate).remove(basemap);
        basemapDateMap.get(startDate).get(endDate).add(basemap);
    }

    public void removeBasemap(Basemap basemap) {
        Integer startDate = basemap.getValidStartDate();
        Integer endDate = basemap.getValidEndDate();

        TreeMap<Integer, ArrayList<Basemap>> endYearMap = basemapDateMap.get(startDate);
        if (endYearMap != null) {
            ArrayList<Basemap> basemaps = endYearMap.get(endDate);
            if (basemaps != null) {
                basemaps.remove(basemap);
                if (basemaps.size() == 0) {
                    endYearMap.remove(endDate);
                    if (endYearMap.size() == 0) {
                        basemapDateMap.remove(startDate);
                    }
                }
            }
        }
    }

    public ArrayList<Basemap> getFlattenedBasemaps() {
        ArrayList<Basemap> flattened = new ArrayList<>();
        Collection<TreeMap<Integer, ArrayList<Basemap>>> flat1 = basemapDateMap.values();
        for (TreeMap<Integer, ArrayList<Basemap>> flat2: flat1) {
            Collection<ArrayList<Basemap>> flat3 = flat2.values();
            for (ArrayList<Basemap> flat4: flat3) {
                flattened.addAll(flat4);
            }
        }
        return flattened;
    }

    public static Object[][] generateTableRows(ArrayList<Basemap> flattenedBasemaps) {
        Object[][] tableRows = new Object[flattenedBasemaps.size()][];
        for (int i=0; i<tableRows.length; i++) {
            Basemap basemap = flattenedBasemaps.get(i);
            tableRows[i] = new Object[4];
            tableRows[i][0] = basemap.getMapName();
            tableRows[i][1] = basemap.getFilePath();
            tableRows[i][2] = basemap.getValidStartDate();
            tableRows[i][3] = basemap.getValidEndDate();
        }
        return tableRows;
    }

    public Basemap getForYears(int start, int end) {
        NavigableMap<Integer, TreeMap<Integer, ArrayList<Basemap>>> navigableMap = basemapDateMap.headMap(start, true);
        for (TreeMap<Integer, ArrayList<Basemap>> endYearMap: navigableMap.values()) {
            for (Integer endYear: endYearMap.navigableKeySet()) {
                if (endYear >= end) {
                    // .get will return null if it cannot find Basemap
                    ArrayList<Basemap> basemaps = endYearMap.get(endYear);
                    return (basemaps != null && basemaps.size() > 0)
                            ? basemaps.get(0)
                            : null;
                }
            }
        }
        return null;
    }
}
