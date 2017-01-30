package org.timetravellersmap.overlay;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;

import java.awt.*;
import java.util.ArrayList;

/**
 * Layer: representation of a screen annotation drawing layer
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
        System.out.println(this.toString() + " draw called");
        for (LayerComponent layerComponent: layerComponents) {
            System.out.println("Trying to draw " + layerComponent.toString());
            layerComponent.draw(graphics2D, mapContent, mapViewport);
        }
    }

    public ReferencedEnvelope getBounds() {
        return null;
    }
}
