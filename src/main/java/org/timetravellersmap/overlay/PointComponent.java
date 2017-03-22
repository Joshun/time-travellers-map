package org.timetravellersmap.overlay;

import com.google.gson.annotations.Expose;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * PointComponent: represents a point to draw with optional radius parameter
 */
public class PointComponent extends LayerComponent{
    @Expose
    private double x;
    @Expose
    private double y;
    private static final int DEFAULT_RADIUS = 1;
    private static final Color DEFAULT_COLOR = new Color(0, 0, 0);
    private final static boolean USE_SCALING = false;
    @Expose
    private double radius;
    @Expose
    private Color color;

    public PointComponent() {
        this(0, 0, 0, new Color(0,0,0));
    }

    public PointComponent(double x, double y, double radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public PointComponent(double x, double y) {
        this(x, y, DEFAULT_RADIUS, DEFAULT_COLOR);
    }

    public PointComponent(double x, double y, double radius) {
        this(x, y, radius, DEFAULT_COLOR);
    }

    public PointComponent(double x, double y, Color color) {
        this(x, y, DEFAULT_RADIUS, color);
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

        System.out.println("scale " + computeMapScale(mapViewport));

        double scaledRadius;
        if (USE_SCALING) {
            scaledRadius = radius / computeMapScale(mapViewport);
        }
        else {
             scaledRadius = radius;
        }

//        double topX = screenX - 2.0*radius;
//        double topY = screenY - 2.0*radius;

        // Convert screen coordinates of midpoint to the topleft coordinates of circle bounding box
        // Distance from midpoint of circle to edge of box is same as diameter
        double topX = screenX - 2.0*scaledRadius;
        double topY = screenY - 2.0*scaledRadius;
        // Width and height of box is twice the diameter / four times radius
        double width = 4.0*scaledRadius;
        double height = 4.0*scaledRadius;

        // Set colour to be the shape's colour
        if (color != null) {
            graphics2D.setColor(color);
        }
        else {
            graphics2D.setColor(new Color(0, 0, 0));
        }

        graphics2D.fill(new Ellipse2D.Double(topX, topY, width, height));
    }

    public void displayAnnotation() {

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }
}
