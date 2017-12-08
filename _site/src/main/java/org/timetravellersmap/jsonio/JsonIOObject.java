/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.jsonio;

import com.google.gson.annotations.Expose;
import org.timetravellersmap.core.Basemap;
import org.timetravellersmap.core.BasemapList;
import org.timetravellersmap.core.event.EventIndex;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.LayerList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * JSONIOObject: representation of a program state to be saved and read from a file
 * Used by JsonIO
 */

public class JsonIOObject {
    @Expose
    private LayerList layerList;
    @Expose
    private EventIndex eventIndex;
    @Expose
    private BasemapList basemapList;
    @Expose
    private String writeTime;
    @Expose
    private String defaultLayerName;
    @Expose
    private int selectedYear;

    public JsonIOObject(LayerList layerList, EventIndex eventIndex, BasemapList basemapList, int selectedYear) {
        this.layerList = layerList;
        this.defaultLayerName = LayerList.DEFAULT_LAYER.getName();
        this.eventIndex = eventIndex;
        this.basemapList = basemapList;
        this.writeTime = LocalDateTime.now().toString();
        this.selectedYear = selectedYear;
    }

    public boolean isReady() {
        return layerList != null && eventIndex != null;
    }

    public LayerList getLayerList() {
        return layerList;
    }

    public EventIndex getEventIndex() {
        return eventIndex;
    }

    public BasemapList getBasemapList() {
        return basemapList;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

}