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
import java.util.Objects;

/**
 * Layer: representation of a screen annotation drawing layer
 * Keeps track of LayerComponents and their corresponding Events
 */
public class Layer extends org.geotools.map.DirectLayer {
    private ArrayList<LayerComponent> layerComponentsToDraw;
    private ArrayList<Event> registeredEvents;
    private ArrayList<Event> eventsToDraw;
    @Expose
    private String name;

    public Layer() {
        this(null);
    }

    public Layer(String layerName) {
        this.name = layerName;

        this.registeredEvents = new ArrayList<>();
        this.layerComponentsToDraw = new ArrayList<>();
        this.eventsToDraw = new ArrayList<>();
    }

    public void registerEvent(Event event) {
        System.out.println("registeredEvents " + registeredEvents);
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

    @Override
    public boolean equals(Object other) {
        if (other instanceof Layer) {
            return name.equals(((Layer) other).getName());
        }
        else {
            return other.equals(this);
        }
    }
}
