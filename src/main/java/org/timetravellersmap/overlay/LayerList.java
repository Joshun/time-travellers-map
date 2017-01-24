package org.timetravellersmap.overlay;

import java.util.HashMap;

/**
 * Created by joshua on 24/01/17.
 */
public class LayerList {
    private HashMap<Integer,Layer> layers = new HashMap();
    private int highestLayerIndex = 0;

    public LayerList() {
    }

    public void draw() {
        for (int i=0; i<highestLayerIndex; i++) {
            if (layers.containsKey(Integer.valueOf(i))) {
                layers.get(i).draw();
            }
        }
    }
}
