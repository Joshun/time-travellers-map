/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.overlay;

import com.google.gson.annotations.Expose;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.timetravellersmap.core.Descriptor;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * LayerComponent: represents components to be drawn on top of the map
 */
public abstract class LayerComponent {
    @Expose
    private Descriptor descriptor;

    public abstract void draw(Graphics2D graphics2D, MapContent mapContent, MapViewport mapViewport);
    public abstract void displayAnnotation();
    public abstract Rectangle2D.Double getBounds(MapContent mapContent, MapViewport mapViewport);

    protected Point2D.Double worldToScreen(Point2D.Double point, MapContent mapContent, MapViewport mapViewport) {
        AffineTransform worldToScreenTransform = mapViewport.getWorldToScreen();
        Point2D.Double result = new Point2D.Double();
        worldToScreenTransform.transform(point, result);
        return result;
    }

    protected double computeMapScale(MapViewport mapViewport) {
        Rectangle screenArea = mapViewport.getScreenArea();
        ReferencedEnvelope bounds = mapViewport.getBounds();
        double scaleX = screenArea.getWidth() / bounds.getWidth();
        return scaleX;
    }

    public LayerComponent(Descriptor layerComponentDescriptor) {
        this.descriptor = layerComponentDescriptor;
    }

    public LayerComponent() {
        this(new Descriptor("", ""));
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

}
