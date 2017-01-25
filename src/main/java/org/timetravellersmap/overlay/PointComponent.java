package org.timetravellersmap.overlay;

import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;

import java.awt.*;

/**
 * Created by joshua on 24/01/17.
 */
public class PointComponent extends LayerComponent{
    private int x;
    private int y;
    private final static int DEFAULT_RADIUS = 1;
    private int radius;

    public PointComponent(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public PointComponent(int x, int y) {
        this(x, y, DEFAULT_RADIUS);
    }

    public String toString() {
        return "PointComponent (" + this.x + "," + this.y + ") radius " + this.radius;
    }

    public boolean equals(PointComponent other) {
        return this.x == other.x && this.y == other.y && this.radius == other.radius;
    }

//    public void draw() {
//        System.out.println("Drawing " + this.toString());
//    }
    public void draw(Graphics2D graphics2D, MapContent mapContent, MapViewport mapViewport) {

    }

    public void displayAnnotation() {

    }
}
