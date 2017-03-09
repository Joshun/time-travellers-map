package org.timetravellersmap.overlay;

import com.google.gson.annotations.Expose;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.map.event.MapLayerEvent;
import org.timetravellersmap.core.event.Event;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Layer: representation of a screen annotation drawing layer
 * Keeps track of LayerComponents and their corresponding Events
 */
public class Layer extends org.geotools.map.DirectLayer {
    private ArrayList<LayerComponent> layerComponentsToDraw = new ArrayList<>();
    private ArrayList<Event> registeredEvents = new ArrayList<>();
    @Expose
    private String name;
    private ArrayList<Event> eventsToDraw = null;

    public Layer(String layerName) {
        this.name = layerName;
    }

    public void registerEvent(Event event) {
        if (! registeredEvents.contains(event)) {
            registeredEvents.add(event);
        }
    }

    public void deregisterEvent(Event event) {
        registeredEvents.remove(event);
    }

    public void makeDrawRequest(LayerComponent layerComponent, Event event) {
        registerEvent(event);
        layerComponentsToDraw.add(layerComponent);
    }

    public void makeDrawRequest(List<LayerComponent> layerComponentList, Event event) {
        registerEvent(event);
        layerComponentsToDraw.addAll(layerComponentList);
    }

    public void clearDrawRequests() {
        layerComponentsToDraw.clear();
        registeredEvents.clear();
    }

    public void draw(Graphics2D graphics2D, MapContent mapContent, MapViewport mapViewport) {
        if (layerComponentsToDraw != null) {
            for (LayerComponent layerComponent: layerComponentsToDraw) {
                layerComponent.draw(graphics2D, mapContent, mapViewport);
            }
        }
    }

    public ArrayList<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    private void fireChanged() {
        fireMapLayerListenerLayerChanged(MapLayerEvent.DATA_CHANGED);
    }

    public ReferencedEnvelope getBounds() {
        return null;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }
}
