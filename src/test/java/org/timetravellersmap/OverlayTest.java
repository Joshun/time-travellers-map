package org.timetravellersmap;

import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.LayerList;
import org.timetravellersmap.overlay.RectangleComponent;

/**
 * Created by joshua on 24/01/17.
 */
public class OverlayTest {
    public static void main(String[] args) {
        LayerList layerList = new LayerList();
        Layer layer = new Layer();

        LayerComponent layerComponent1 = new RectangleComponent(0, 0, 20, 20);
        LayerComponent layerComponent2 = new RectangleComponent(20, 20, 40, 40);
        layer.addComponent(layerComponent1);
        layer.addComponent(layerComponent2);
        layerList.addLayer(layer, 0);
        layerList.draw();

        System.out.println("Remove 2nd layercomponent");
        layer.removeComponent(layerComponent2);
        layerList.draw();

        System.out.println("Add new layer");
        Layer layer2 = new Layer();
        LayerComponent layerComponent3 = new RectangleComponent(50, 50, 100, 100);
        layer2.addComponent(layerComponent3);
        layerList.addLayer(layer2, 1);
        layerList.draw();

        System.out.println("Remove layer");
        layerList.removeLayer(0);
        layerList.draw();
    }
}
