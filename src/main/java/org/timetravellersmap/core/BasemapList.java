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
    private HashMap<Integer,HashMap<Integer, ArrayList<Basemap>>> basemapDateMap = new HashMap<>();

    public void addBasemap(Basemap basemap) {
        Integer startDate = basemap.getValidStartDate();
        Integer endDate = basemap.getValidEndDate();

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

    public ArrayList<Basemap> getValidBasemaps(int validStartDate, int validEndDate) {
        return basemapDateMap.get(validStartDate).get(validEndDate);
    }

    public Basemap getValidBasemap(int validStartDate, int validEndDate) {
        return getValidBasemaps(validStartDate, validEndDate).get(0);
    }

    public ArrayList<Basemap> getFlattenedBasemaps() {
        ArrayList<Basemap> flattened = new ArrayList<>();
        Collection<HashMap<Integer, ArrayList<Basemap>>> flat1 = basemapDateMap.values();
        for (HashMap<Integer, ArrayList<Basemap>> flat2: flat1) {
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
            tableRows[i] = new Object[4];
            tableRows[i][0] = basemap.getMapName();
            tableRows[i][1] = basemap.getFilePath();
//            Calendar start = new GregorianCalendar();
//            start.setTime(basemap.getValidStartDate());
//            Calendar end = new GregorianCalendar();
//            end.setTime(basemap.getValidEndDate());
            tableRows[i][2] = basemap.getValidStartDate();
            tableRows[i][3] = basemap.getValidEndDate();
        }
        return tableRows;
    }
}
