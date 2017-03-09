package org.timetravellersmap;

import com.google.gson.annotations.Expose;
import org.timetravellersmap.core.event.EventIndex;
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
    private String writeTime;
    @Expose
    private String defaultLayerName;

    public JsonIOObject(LayerList layerList, EventIndex eventIndex) {
        this.layerList = layerList;
        this.defaultLayerName = layerList.DEFAULT_LAYER.getName();
        this.eventIndex = eventIndex;
        this.writeTime = LocalDateTime.now().toString();
    }
}