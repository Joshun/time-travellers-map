package org.timetravellersmap.overlay;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.map.event.MapLayerEvent;
import org.timetravellersmap.core.event.Event;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Layer: representation of a screen annotation drawing layer
 * Keeps track of LayerComponents and their corresponding Events
 */
public class Layer extends org.geotools.map.DirectLayer {
//    private ArrayList<LayerComponent> eventLayerComponents = new ArrayList<>();
    private HashMap<Event, ArrayList<LayerComponent>>  eventLayerComponents = new HashMap<>();
    private String name;
    private ArrayList<Event> eventsToDraw = null;
//    private MapContent mapContent;

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
        if (!eventLayerComponents.containsKey(associatedEvent)) {
            eventLayerComponents.put(associatedEvent, new ArrayList<LayerComponent>());
        }
        eventLayerComponents.get(associatedEvent).addAll(layerComponents);
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
                for (LayerComponent layerComponent : layerComponentsToDraw) {
                    layerComponent.draw(graphics2D, mapContent, mapViewport);
                }
            }
        }
//        System.out.println(this.toString() + " draw called");
//        for (LayerComponent layerComponent: eventLayerComponents) {
//            System.out.println("Trying to draw " + layerComponent.toString());
//            layerComponent.draw(graphics2D, mapContent, mapViewport);
//        }
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
