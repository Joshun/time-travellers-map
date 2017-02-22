package org.timetravellersmap.overlay;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.map.event.MapLayerEvent;
import org.timetravellersmap.core.event.Event;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Layer: representation of a screen annotation drawing layer
 * Keeps track of LayerComponents and their corresponding Events
 */
public class Layer extends org.geotools.map.DirectLayer {
    private HashMap<Event, ArrayList<LayerComponent>>  eventLayerComponents = new HashMap<>();
    private String name;
    private ArrayList<Event> eventsToDraw = null;

    public Layer(String layerName) {
        this.name = layerName;
    }

    public void addComponent(LayerComponent component, Event associatedEvent) {
        ArrayList<LayerComponent> componentArrayList = eventLayerComponents.get(associatedEvent);
        if (componentArrayList == null) {
            componentArrayList = new ArrayList<>();
            componentArrayList.add(component);
            eventLayerComponents.put(associatedEvent, componentArrayList);
        }
        else {
            componentArrayList.add(component);
        }
        fireChanged();
        System.out.println(eventLayerComponents);
    }

    public void addEventLayerComponents(ArrayList<LayerComponent> layerComponents, Event associatedEvent) {
        if (layerComponents == null) {
            return;
        }
        eventLayerComponents.put(associatedEvent, layerComponents);
    }

    public void removeComponent(LayerComponent component, Event associatedEvent) {
        ArrayList<LayerComponent> componentArrayList = eventLayerComponents.get(associatedEvent);
        componentArrayList.remove(component);
        fireChanged();
    }

    public void setEventsToDraw(ArrayList<Event> eventsToDraw) {
        this.eventsToDraw = eventsToDraw;
        fireChanged();
    }
    
    public void clearEventsToDraw() {
        this.eventsToDraw = null;
        fireChanged();
    }

    public void draw(Graphics2D graphics2D, MapContent mapContent, MapViewport mapViewport) {
        if (eventsToDraw != null) {
            for (Event event : eventsToDraw) {
                ArrayList<LayerComponent> layerComponentsToDraw = eventLayerComponents.get(event);
                if (layerComponentsToDraw == null) {
                    continue;
                }
                for (LayerComponent layerComponent : layerComponentsToDraw) {
                    layerComponent.draw(graphics2D, mapContent, mapViewport);
                }
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Layer) {
            return ((Layer) other).getName().equals(this.name);
        }
        else {
            return other.equals(this);
        }
    }

    public Set<Event> getAllEvents() {
        return eventLayerComponents.keySet();
    }

    public ArrayList<LayerComponent> getEventLayerComponents(Event event) {
        return eventLayerComponents.get(event);
    }

    public void removeEventComponents(Event event) {
        eventLayerComponents.remove(event);
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
