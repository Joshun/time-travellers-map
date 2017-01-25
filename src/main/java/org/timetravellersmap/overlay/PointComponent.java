package org.timetravellersmap.overlay;

import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

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
        double screenX, screenY;
        Point2D.Double point = worldToScreen(new Point2D.Double(x, y), mapContent, mapViewport);
        screenX = point.getX();
        screenY = point.getY();

//        double topX = screenX - 2.0*radius;
//        double topY = screenY - 2.0*radius;

        // Convert screen coordinates of midpoint to the topleft coordinates of circle bounding box
        // Distance from midpoint of circle to edge of box is same as diameter
        double topX = screenX - 2.0*radius;
        double topY = screenY - 2.0*radius;
        // Width and height of box is twice the diameter / four times radius
        double width = 4.0*radius;
        double height = 4.0*radius;

        graphics2D.fill(new Ellipse2D.Double(topX, topY, width, height));
    }

    public void displayAnnotation() {

    }
}
