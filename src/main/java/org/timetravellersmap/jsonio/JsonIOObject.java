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
 * Created by joshua on 09/03/17.
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

    public JsonIOObject(LayerList layerList, EventIndex eventIndex, BasemapList basemapList) {
        this.layerList = layerList;
        this.defaultLayerName = LayerList.DEFAULT_LAYER.getName();
        this.eventIndex = eventIndex;
        this.basemapList = basemapList;
        this.writeTime = LocalDateTime.now().toString();
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

    @Override
    public String toString() {
        LayerComponent test = eventIndex.getStartEventsForYear(2017).get(0).getLayerComponents().get(0);
        return "JSONIOObject " + test;
    }
}