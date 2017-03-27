package org.timetravellersmap.overlay;


import com.google.gson.annotations.Expose;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.timetravellersmap.core.event.EventChangeListener;
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
    // List of layers, in render order
    @Expose
    private ArrayList<Layer> layers = new ArrayList<>();

    // layerName, index mapping
    @Expose
    private HashMap<String, Integer> layerMapping = new HashMap<>();

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
        layerMapping.put("Default", 0);
        updateMapContent();
    }

    public void addLayer(Layer layer) throws LayerAlreadyExistsException {
        if (layerMapping.containsKey(layer.getName())) {
            throw new LayerAlreadyExistsException("failed to add layer: " + layer.getName());
        }
        layers.add(layer);
        layerMapping.put(layer.getName(), layers.indexOf(layer));
        updateMapContent();
    }

    public void removeLayer(String layerName) throws AttemptRemoveDefaultLayerException {
        if (DEFAULT_LAYER.getName().equals(layerName)) {
            throw new AttemptRemoveDefaultLayerException("attempt to remove Default layer " + DEFAULT_LAYER);
        }

        ArrayList<Layer> layersToRemove = new ArrayList<>();
        for (Layer layer: layers) {
            if (layer.getName().equals(layerName)) {
                layerMapping.remove(layer.getName());
                LOGGER.info("Remove layer " + layerName);
                layersToRemove.add(layer);
            }
        }
        for (Layer layer: layersToRemove) {
            layer.preDispose();
            layer.dispose();
            layers.remove(layer);
        }

        updateMapContent();
    }

    public void swapLayers(Layer layer1, Layer layer2) {
        List<org.geotools.map.Layer> mapContentLayers = mapContent.layers();
        LOGGER.info("swapping layers \"" + layer1 + "\" and \"" + layer2 + "\"");
        Collections.swap(layers, layers.indexOf(layer1), layers.indexOf(layer2));

        // Update layer mappings
        layerMapping.put(layer1.getName(), layers.indexOf(layer1));
        layerMapping.put(layer2.getName(), layers.indexOf(layer2));

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

//    public Layer getLayer(Layer layer) {
//        return layers.get(layers.indexOf(layer));
//    }

    public Layer getLayer(String layerName) {
        LOGGER.info("getLayer " + layerName);
        Object mapping =  layerMapping.get(layerName);
        if (mapping == null) {
            return null;
        }
        else {
            return layers.get(layerMapping.get(layerName));
        }
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
            mapContentLayers.get(n).preDispose();
            mapContentLayers.remove(n);
        }
    }

    // Copies user layers to the mapcontent layers
    // We start at mapcontent layer index 1 as 0 is the feature (map) layer
    public void updateMapContent() {
        LOGGER.info("updateMapContent() called");
        List<org.geotools.map.Layer> mapContentLayers = mapContent.layers();

        // If the "managed" list of layers has shrunk to smaller than the mapContentLayers, "purge" mapContent layers
        if (layers.size()+1 < mapContentLayers.size()) {
            purgeMapContent();
        }

        for (int i=0; i<layers.size(); i++) {
            if (i+BASE_LAYER_INDEX+1>=mapContentLayers.size()) {
                mapContentLayers.add(layers.get(i));
            }
            else {
                // preDispose() is needed to notify Geotools that the layer is being removed / changed
                mapContentLayers.get(i + BASE_LAYER_INDEX + 1).preDispose();
                mapContentLayers.get(i + BASE_LAYER_INDEX + 1).dispose();
                mapContentLayers.set(i + BASE_LAYER_INDEX + 1, layers.get(i));
            }
        }
    }

    public void setEventsToDraw(ArrayList<Event> events) {
        for (Layer layer: layers) {
            layer.clearDrawRequests();
        }

        for (Event event: events) {
            // Check that Layer exists and that the event's layer hasn't been deleted
            Layer layer = getLayer(event.getLayerName());
            if (layer == null) {
                // Event layer must have been deleted, tell it to use the default layer
                event.setLayerName(DEFAULT_LAYER.getName());
                // Make requests to default layer instead
                layer = DEFAULT_LAYER;
            }
            layer.makeDrawRequest(event.getLayerComponents());
        }
        updateMapContent();
    }

    public void moveEventToLayer(Event event, String newLayerName) {
        Layer newLayer = getLayer(newLayerName);

        LOGGER.info("move event \"" + event + "\" to " + newLayer);
        Layer currentLayer = getLayer(event.getLayerName());
        if (newLayerName.equals(currentLayer.getName())) {
            return;
        }
        event.setLayerName(newLayer.getName());
    }

    public void forceRepaint() {
        mapContent.getViewport().setBounds(new ReferencedEnvelope(
                -180.0,
                180.0,
                -90.0,
                90.0,
                DefaultGeographicCRS.WGS84
        ));
    }

    public void setMapContent(MapContent mapContent) {
        this.mapContent = mapContent;
    }

    public void setBaseLayer(org.geotools.map.Layer layer) {
//        this.baseLayer.preDispose();
//        this.baseLayer.dispose();
        this.baseLayer = layer;
        mapContent.layers().set(0, baseLayer);
    }
}
