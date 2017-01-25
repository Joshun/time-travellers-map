package org.timetravellersmap.overlay;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by joshua on 23/01/17.
 */
public class Layer extends org.geotools.map.DirectLayer {
    private ArrayList<LayerComponent> layerComponents = new ArrayList<>();
//    private MapContent mapContent;

//    public Layer(MapContent mapContent) {
//        this.mapContent = mapContent;
//    }

    public void addComponent(LayerComponent component) {
        layerComponents.add(component);
    }

    public void removeComponent(LayerComponent component) {
        layerComponents.remove(component);
    }

//    public void draw() {
//        for (LayerComponent layerComponent: layerComponents) {
//            layerComponent.draw();
//        }
//    }

    public void draw(Graphics2D graphics2D, MapContent mapContent, MapViewport mapViewport) {
        for (LayerComponent layerComponent: layerComponents) {
            layerComponent.draw(graphics2D, mapContent, mapViewport);
        }
    }

    public ReferencedEnvelope getBounds() {
        return null;
    }
}
