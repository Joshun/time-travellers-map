package org.timetravellersmap.overlay;

import org.geotools.map.MapContent;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by joshua on 24/01/17.
 */
public class LayerList {
    private TreeMap<Integer,Layer> layers = new TreeMap<>();
    private MapContent mapContent;

    public LayerList(MapContent mapContent) {
        this.mapContent = mapContent;
    }

    public void addLayer(Layer layer, int orderIndex) {
        layers.put(Integer.valueOf(orderIndex), layer);
    }

    public void removeLayer(int orderIndex) {

        layers.remove(Integer.valueOf(orderIndex));
    }

//    public void draw() {
//        for (Map.Entry<Integer,Layer> layerEntry: layers.entrySet()) {
//            int orderIndex = layerEntry.getKey().intValue();
//            System.out.println("Drawing layer " + orderIndex);
//            Layer layer = layerEntry.getValue();
//            layer.draw();
//        }
//    }
}
