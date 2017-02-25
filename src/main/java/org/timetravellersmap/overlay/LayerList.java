package org.timetravellersmap.overlay;


import org.geotools.map.MapContent;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.core.event.Event;

import java.util.*;
import java.util.logging.Logger;

/**
 * LayerList: represents a list of layers drawn on the map and their order
 * Has utility functions for adding, removing and changing layer order, and moving an event to a different layer
 */
public class LayerList {
    private final static Logger LOGGER = Logger.getLogger(LayerList.class.getName());
//    private TreeMap<Layer,Integer> layers = new TreeMap<>();
    private ArrayList<Layer> layers = new ArrayList<Layer>();
    private MapContent mapContent;

    // This is the default layer, for usability and stability this cannot be removed
    public final static Layer DEFAULT_LAYER = new Layer("Default");

    // Base layer index and base layer, this is the layer containing the map itself
    public final static int BASE_LAYER_INDEX = 0;
    private org.geotools.map.Layer baseLayer = null;

    public LayerList(MapContent mapContent, org.geotools.map.Layer baseLayer) {
        this.baseLayer = baseLayer;
        mapContent.addLayer(baseLayer);
        this.mapContent = mapContent;
        // By default there is one layer
        layers.add(DEFAULT_LAYER);
        updateMapContent();
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
        mapContent.addLayer(layer);
        updateMapContent();
    }

    public void removeLayer(Layer layer) {
        if (layer != DEFAULT_LAYER) {
            // Set all of its events to the default layer
            for (Event event: layer.getRegisteredEvents()) {
                event.setLayer(DEFAULT_LAYER);
            }
            layers.remove(layer);
        }
        else {
            LOGGER.warning("Refusing request to remove default layer.");
        }
        updateMapContent();
    }

    public void swapLayers(Layer layer1, Layer layer2) {
        LOGGER.info("swapping layers \"" + layer1 + "\" and \"" + layer2 + "\"");
        Collections.swap(layers, layers.indexOf(layer1), layers.indexOf(layer2));
        updateMapContent();
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

    public Layer[] getLayers() {
        Layer[] layerArr = new Layer[layers.size()];
        layerArr = layers.toArray(layerArr);
        return layerArr;
    }

    public ArrayList<Layer> getLayersArrayList() {
        return layers;
    }

    public int getCount() {
        return layers.size();
    }


    // Removes all layers from rendered list apart from base layer
    // Called if layers have been removed
    private void purgeMapContent() {
        LOGGER.info("purgeMapContent() called");
        List<org.geotools.map.Layer> mapContentLayers = mapContent.layers();
        int size = mapContentLayers.size();
        for (int n=size-1; n>0; n--) {
            mapContentLayers.remove(n);
        }
    }

    // Copies user layers to the mapcontent layers
    // We start at mapcontent layer index 1 as 0 is the feature (map) layer
    public void updateMapContent() {
        LOGGER.info("updateMapContent() called");
        List<org.geotools.map.Layer> mapContentLayers = mapContent.layers();

        // If the "managed" list of layers has shrunk to smaller than the mapContentLayers, "purge" mapContent layers
        if (layers.size() < mapContentLayers.size()) {
            purgeMapContent();
        }

        for (int i=0; i<layers.size(); i++) {
            if (i+BASE_LAYER_INDEX+1>=mapContentLayers.size()) {
                mapContentLayers.add(layers.get(i));
            }
            else {
                mapContentLayers.set(i+BASE_LAYER_INDEX+1, layers.get(i));
            }
        }
    }

    public void setEventsToDraw(ArrayList<Event> events) {
        for (Layer layer: layers) {
            layer.clearDrawRequests();
        }

        for (Event event: events) {
            Layer layer = event.getLayer();
            layer.makeDrawRequest(event.getLayerComponents(), event);
        }
        updateMapContent();
//        for (Layer layer: layers) {
//            layer.setEventsToDraw(events);
//        }
    }

    public void moveEventToLayer(Event event, Layer newLayer) {
        LOGGER.info("move event \"" + event + "\" to " + newLayer);
        Layer currentLayer = event.getLayer();
        if (newLayer == currentLayer) {
            return;
        }
        event.setLayer(newLayer);
//        ArrayList<LayerComponent> layerComponents = currentLayer.getEventLayerComponents(event);
//        ArrayList<LayerComponent> layerComponents = event.
//        newLayer.addEventLayerComponents(layerComponents, event);
//        currentLayer.removeEventComponents(event);
    }

}
