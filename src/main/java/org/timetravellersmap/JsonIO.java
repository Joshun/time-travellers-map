package org.timetravellersmap;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonWriter;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.core.event.EventIndex;
import org.timetravellersmap.overlay.LayerList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

/**
 * Created by joshua on 09/03/17.
 */
public class JsonIO {
    private final static Logger LOGGER = Logger.getLogger(JsonIO.class.getName());
    private Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();

    private LayerList layerList;
    private EventIndex eventIndex;

    public JsonIO(LayerList layerList, EventIndex eventIndex) {
        this.layerList = layerList;
        this.eventIndex = eventIndex;
    }

    public void loadJson(File jsonFile) {
    }

    public void saveJson(String jsonFile) {
        saveJson(new File(jsonFile));
    }

    public void saveJson(File jsonFile) {
        try {
            FileWriter fileWriter = new FileWriter(jsonFile);
            JsonWriter jsonWriter = new JsonWriter(fileWriter);
            jsonWriter.setIndent("  ");

            gson.toJson(new JsonIOObject(layerList, eventIndex), JsonIOObject.class, jsonWriter);

            jsonWriter.close();
        }
        catch (java.io.IOException e) {
            LOGGER.warning("Failed to write to file " + jsonFile.getName() + ": " + e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Layer baseLayer = new org.timetravellersmap.overlay.Layer("baselayer");
        MapContent mapContent = new MapContent();
        LayerList layerList = new LayerList(mapContent, baseLayer);
        EventIndex eventIndex = new EventIndex();
        eventIndex.addEvent(new Event(new GregorianCalendar(), new GregorianCalendar(), new Descriptor("event name", "event description")));

        JsonIO jsonIO = new JsonIO(layerList, eventIndex);
        jsonIO.saveJson("test.json");
    }
}
