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

//    public void removeLayer(int orderIndex) {
//
//        layers.remove(Integer.valueOf(orderIndex));
//    }

    public void removeLayer(Layer layer) {
        if (layer != DEFAULT_LAYER) {
            // Set all of its events to the default layer
            for (Event event: layer.getAllEvents()) {
                event.setLayer(DEFAULT_LAYER);
            }
            layers.remove(layer);
        }
        else {
            System.out.println("Refusing request to remove default layer.");
        }
        updateMapContent();
//        mapContent.removeLayer(layer);
    }

    public void swapLayers(Layer layer1, Layer layer2) {
        System.out.println("layers " + mapContent.layers());
        System.out.println("size " + layers.size());
        Collections.swap(layers, layers.indexOf(layer1), layers.indexOf(layer2));
//
//        // Swap using geotools mapcontent
//        List<org.geotools.map.Layer> mapContentLayers = mapContent.layers();
//        int layer1Index = mapContentLayers.indexOf(layer1);
//        int layer2Index = mapContentLayers.indexOf(layer2);
//        int tempLayer1Index = mapContentLayers.size();
////        mapContent.moveLayer(layer1Index, tempLayer1Index);
////        mapContent.moveLayer(layer2Index, layer1Index);
////        mapContent.moveLayer(tempLayer1Index, layer2Index);
//        mapContent.moveLayer(layer1Index, layer2Index);

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

//    public void updateMapContent(MapContent mapContent) {
//        for (Layer layer: layers) {
//            mapContent.removeLayer(layer);
//        }
//        mapContent.addLayers(layers);
//    }

    // Copies user layers to the mapcontent layers
    // We start at mapcontent layer index 1 as 0 is the feature (map) layer

    private void purgeMapContent() {
        List<org.geotools.map.Layer> mapContentLayers = mapContent.layers();
//        org.geotools.map.Layer baseLayer = mapContent.layers().get(0);
//        mapContentLayers.clear();
//        mapContentLayers.add(baseLayer);
        int size = mapContentLayers.size();
        for (int n=size-1; n>0; n--) {
            mapContentLayers.remove(n);
        }
    }

    public void updateMapContent() {
        List<org.geotools.map.Layer> mapContentLayers = mapContent.layers();

        // If the "managed" list of layers has shrunk to smaller than the mapContentLayers, "purge" mapContent layers
        if (layers.size() < mapContentLayers.size()) {
            purgeMapContent();
        }

        for (int i=0; i<layers.size(); i++) {
            System.out.println("size="+layers.size()+" i="+i);
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
            layer.setEventsToDraw(events);
        }
    }

    public void moveEventToLayer(Event event, Layer newLayer) {
        System.out.println("move " + event + " to " + newLayer);
        Layer currentLayer = event.getLayer();
        if (newLayer == currentLayer) {
            return;
        }
        event.setLayer(newLayer);
        ArrayList<LayerComponent> layerComponents = currentLayer.getEventLayerComponents(event);
        newLayer.addEventLayerComponents(layerComponents, event);
        currentLayer.removeEventComponents(event);
    }

}
