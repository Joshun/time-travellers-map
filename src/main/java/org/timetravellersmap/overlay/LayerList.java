package org.timetravellersmap.overlay;


import org.geotools.map.MapContent;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.core.event.Event;

import java.util.*;

/**
 * LayerList: represents a list of layers drawn on the map and their order
 * Currently not used
 */
public class LayerList {
//    private TreeMap<Layer,Integer> layers = new TreeMap<>();
    private ArrayList<Layer> layers = new ArrayList<Layer>();
    private MapContent mapContent;

    private MapFrame mapFrame;
    // This is the default layer, for usability and stability this cannot be removed
    public final static Layer DEFAULT_LAYER = new Layer("Default");

    public LayerList(MapContent mapContent) {
        this.mapContent = mapContent;
        // By default there is one layer
        layers.add(DEFAULT_LAYER);
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
        updateMapContent(mapContent);
    }

//    public void removeLayer(int orderIndex) {
//
//        layers.remove(Integer.valueOf(orderIndex));
//    }

    public void removeLayer(Layer layer) {
        if (layer != DEFAULT_LAYER) {
            layers.remove(layer);
        }
        else {
            System.out.println("Refusing request to remove default layer.");
        }
        updateMapContent(mapContent);
    }

    public void swapLayers(Layer layer1, Layer layer2) {
        Collections.swap(layers, layers.indexOf(layer1), layers.indexOf(layer2));
        updateMapContent(mapContent);
    }

    public void moveLayerDown(Layer layer) {
        int layerIndex = layers.indexOf(layer);
        if (moreThanOneLayer() && layerIndex != layers.size()-1) {
            Layer layerToSwap = layers.get(layerIndex+1);
            swapLayers(layer, layerToSwap);
        }
    }

    public void moveLayerUp(Layer layer) {
        int layerIndex = layers.indexOf(layer);
        if (moreThanOneLayer() && layerIndex != 0) {
            Layer layerToSwap = layers.get(layerIndex-1);
            swapLayers(layer, layerToSwap);
        }
    }

    private boolean moreThanOneLayer() {
        return layers.size() > 1;
    }

    public int getLayerPosition(Layer layer) {
        return layers.indexOf(layer);
    }

    public Layer getLayerAtPosition(int position) {
        return layers.get(position);
    }

    public int getCount() {
        return layers.size();
    }

    public void draw(MapContent mapContent, ArrayList<Event> currentEvents) {

//        for (Map.Entry<Integer,Layer> layerEntry: layers.entrySet()) {
//            int orderIndex = layerEntry.getKey().intValue();
//            System.out.println("Drawing layer " + orderIndex);
//            Layer layer = layerEntry.getValue();
//            layer.draw();
//        }
    }

    public void updateMapContent(MapContent mapContent) {
        for (Layer layer: layers) {
            mapContent.removeLayer(layer);
        }
        mapContent.addLayers(layers);
    }

    public void setEventsToDraw(ArrayList<Event> events) {
        for (Layer layer: layers) {
            layer.setEventsToDraw(events);
        }
    }

    public void moveEventToLayer(Event event, Layer newLayer) {
        Layer currentLayer = event.getLayer();
        ArrayList<LayerComponent> layerComponents = currentLayer.getEventLayerComponents(event);
        newLayer.addEventLayerComponents(layerComponents, event);
        currentLayer.removeEventComponents(event);
        event.setLayer(newLayer);
    }

}
