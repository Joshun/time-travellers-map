package org.timetravellersmap.overlay;

import com.google.gson.annotations.Expose;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.map.event.MapLayerEvent;
import org.timetravellersmap.core.event.Event;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Layer: representation of a screen annotation drawing layer
 * Keeps track of LayerComponents and their corresponding Events
 */
public class Layer extends org.geotools.map.DirectLayer {
    private ArrayList<LayerComponent> layerComponentsToDraw;
    @Expose
    private String name;

    public Layer() {
        this("");
    }

    public Layer(String layerName) {
        this.name = layerName;

        this.layerComponentsToDraw = new ArrayList<>();
    }

    public void makeDrawRequest(LayerComponent layerComponent) {
        layerComponentsToDraw.add(layerComponent);
    }

    public void makeDrawRequest(List<LayerComponent> layerComponentList) {
        layerComponentsToDraw.addAll(layerComponentList);
    }

    public void clearDrawRequests() {
        layerComponentsToDraw.clear();
    }

    public void draw(Graphics2D graphics2D, MapContent mapContent, MapViewport mapViewport) {
        if (layerComponentsToDraw != null) {
            for (LayerComponent layerComponent: layerComponentsToDraw) {
                layerComponent.draw(graphics2D, mapContent, mapViewport);
            }
        }
    }

    private void fireChanged() {
        fireMapLayerListenerLayerChanged(MapLayerEvent.DATA_CHANGED);
    }

    public ReferencedEnvelope getBounds() {
        return null;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Layer) {
            return name.equals(((Layer) other).getName());
        }
        else {
            return other.equals(this);
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + layerComponentsToDraw.hashCode();
        return result;
    }

}
