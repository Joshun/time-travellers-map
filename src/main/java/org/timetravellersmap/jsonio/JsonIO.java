package org.timetravellersmap.jsonio;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.timetravellersmap.core.BasemapList;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.core.event.EventIndex;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.LayerList;
import org.timetravellersmap.overlay.PointComponent;
import org.timetravellersmap.overlay.RectangleComponent;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

/**
 * JsonIO: load and save program state to JSON file
 */
public class JsonIO {
    private final static Logger LOGGER = Logger.getLogger(JsonIO.class.getName());
    private Gson gson;

    public JsonIO() {
        // RuntimeTypeAdapterFactory is needed to properly handle polymorphic types
        RuntimeTypeAdapterFactory<LayerComponent> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(LayerComponent.class)
                .registerSubtype(PointComponent.class)
                .registerSubtype(RectangleComponent.class);

        // Set up new Gson object for serialization and deserialization
        // serializeNulls: ensure nulls are stored
        // excludeFieldsWithoutExposeAnnotation: only fields with @Expose are included
        // registerTypeAdapterFactory: register the runtimeTypeAdaptorFactory for handling polymorphic types
        gson = new GsonBuilder()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(Color.class, new ColorSerializer())
                .registerTypeAdapter(Color.class, new ColorDeserializer())
                .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
                .create();
    }

    public JsonIOObject loadJson(String jsonFile) {
        return loadJson(new File(jsonFile));
    }

    public JsonIOObject loadJson(File jsonFile) {
        try {
            FileReader fileReader = new FileReader(jsonFile);
            JsonReader jsonReader = new JsonReader(fileReader);
            JsonIOObject readIn = gson.fromJson(jsonReader, JsonIOObject.class);
            LOGGER.info("Load success: " + jsonFile.getName());
            return readIn;
        }
        catch (java.io.FileNotFoundException e) {
            LOGGER.warning("File not found " + jsonFile.getPath());
            e.printStackTrace();
            return null;
        }
    }

    public void saveJson(String jsonFile, JsonIOObject jsonIOObject) {
        saveJson(new File(jsonFile), jsonIOObject);
    }

    public void saveJson(File jsonFile, JsonIOObject jsonIOObject) {
        try {
            FileWriter fileWriter = new FileWriter(jsonFile);
            JsonWriter jsonWriter = new JsonWriter(fileWriter);
            jsonWriter.setIndent("  ");

            gson.toJson(jsonIOObject, JsonIOObject.class, jsonWriter);
            LOGGER.info("Save success: " + jsonFile.getPath());
            jsonWriter.close();
        }
        catch (java.io.IOException e) {
            LOGGER.warning("Failed to write to file " + jsonFile.getPath() + ": " + e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Layer baseLayer = new org.timetravellersmap.overlay.Layer("baselayer");
        MapContent mapContent = new MapContent();
        LayerList layerList = new LayerList(mapContent, baseLayer);
        EventIndex eventIndex = new EventIndex();
        BasemapList basemapList = new BasemapList();
        Event event = new Event(new GregorianCalendar(), new GregorianCalendar(), new Descriptor("event name", "event description"));
        PointComponent pointComponent = new PointComponent(1, 1, 4);
        event.addLayerComponent(pointComponent);
        eventIndex.addEvent(event);

        JsonIO jsonIO = new JsonIO();
        System.out.println("Generating JSONIOObject...");
        JsonIOObject jsonIOObject = new JsonIOObject(layerList, eventIndex, basemapList);
        System.out.println(jsonIOObject);
        System.out.println("Saving...");
        jsonIO.saveJson("test.json", jsonIOObject);
        System.out.println("Loading...");
        JsonIOObject newIOObject = jsonIO.loadJson("test.json");
        System.out.println(newIOObject);
    }
}
