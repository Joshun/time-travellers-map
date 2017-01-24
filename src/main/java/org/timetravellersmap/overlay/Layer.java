package org.timetravellersmap.overlay;

import java.util.ArrayList;

/**
 * Created by joshua on 23/01/17.
 */
public class Layer {
    private ArrayList<LayerComponent> layerComponents = new ArrayList<>();

    public Layer () {
    }

    public void addComponent(LayerComponent component) {
        layerComponents.add(component);
    }

    public void removeComponent(LayerComponent component) {
        layerComponents.remove(component);
    }

    public void draw() {
        for (LayerComponent layerComponent: layerComponents) {
            layerComponent.draw();
        }
    }
}
