package org.timetravellersmap.overlay;

import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.timetravellersmap.Annotation;

import java.awt.*;

/**
 * Created by joshua on 23/01/17.
 */
public abstract class LayerComponent {
    private Annotation layerAnnotation;
    public abstract void draw(Graphics2D graphics2D, MapContent mapContent, MapViewport mapViewport);
    public abstract void displayAnnotation();

}
