package org.timetravellersmap.overlay;

import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.timetravellersmap.core.event.Event;

import java.util.ArrayList;

/**
 * LayerComponentSelector: utilities to retrieve appropriate LayerComponent from mouse position
 */
public class LayerComponentSelector {
    public static LayerComponent selectLayerComponent(MapContent mapContent, MapViewport mapViewport, double mouseX, double mouseY, ArrayList<Event> selectedEvents) {
        for (Event event: selectedEvents) {
            for (LayerComponent layerComponent: event.getLayerComponents()) {
                // Do bounding-box intersection testing for each
                if (layerComponent.getBounds(mapContent, mapViewport).contains(mouseX, mouseY)) {
                    return layerComponent;
                }
            }
        }
        return null;
    }

    public static Event whichEventBelongs(LayerComponent layerComponent, ArrayList<Event> currentEvents) {
        for (Event event: currentEvents) {
            for (LayerComponent layerComponent1: event.getLayerComponents()) {
                if (layerComponent1.equals(layerComponent)) {
                    return event;
                }
            }
        }
        return null;
    }
}
