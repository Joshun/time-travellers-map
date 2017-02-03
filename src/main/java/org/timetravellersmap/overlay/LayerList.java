package org.timetravellersmap.overlay;


import org.geotools.map.MapContent;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.timeline.Event;

import java.util.*;

/**
 * LayerList: represents a list of layers drawn on the map and their order
 * Currently not used
 */
public class LayerList {
//    private TreeMap<Layer,Integer> layers = new TreeMap<>();
    private ArrayList<Layer> layers = new ArrayList<Layer>();
//    private MapContent mapContent;

    private MapFrame mapFrame;
    // This is the default layer, for usability and stability this cannot be removed
    public final static Layer DEFAULT_LAYER = new Layer("Default");

    public LayerList() {
        // By default there is one layer
        layers.add(DEFAULT_LAYER);
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
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
    }

    public void swapLayers(Layer layer1, Layer layer2) {
        Collections.swap(layers, layers.indexOf(layer1), layers.indexOf(layer2));
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

}
