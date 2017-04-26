package org.timetravellersmap.overlay;

import com.google.gson.annotations.Expose;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.timetravellersmap.core.Descriptor;

import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;

/**
 * RectangleComponent: represents a bounding box rectangle to draw
 */
public class RectangleComponent extends LayerComponent {
    // Bounding box coordinates
    // x1,y1: topleft
    // x2,y2: bottomright
    @Expose
    private double x1;
    @Expose
    private double y1;
    @Expose
    private double x2;
    @Expose
    private double y2;
    @Expose
    private Color color;
    @Expose
    private float strokeWidth;

    private static final Color DEFAULT_COLOR = new Color(0, 0, 0);
    private static final float DEFAULT_STROKE_WIDTH = 1;

    public RectangleComponent() {
        this(0, 0, 0, 0);
    }

    public RectangleComponent(double x1, double y1, double x2, double y2) {
        this(x1, y1, x2, y2, DEFAULT_COLOR);
    }

    public RectangleComponent(double x1, double y1, double x2, double y2, Color color) {
        this(x1, y1, x2, y2, color, DEFAULT_STROKE_WIDTH);
    }

    public RectangleComponent(double x1, double y1, double x2, double y2, Color color, float strokeWidth) {
        this(x1, y1, x2, y2, color, strokeWidth, new Descriptor());
    }

    public RectangleComponent(double x1, double y1, double x2, double y2, Color color, float strokeWidth, Descriptor descriptor) {
        super(descriptor);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    public boolean equals(RectangleComponent other) {
        return
            this.x1 == other.x1 &&
            this.x2 == other.x2 &&
            this.y1 == other.y1 &&
            this.y2 == other.y2;
    }

    public String toString() {
        return "RectangleComponent (" + this.x1 + "," + this.y1 + ") (" + this.x2 + "," + this.y2 + ")";
    }

//    public void draw() {
//        System.out.println("Draw rectangle with coords" + this.toString());
//    }

    public void draw(Graphics2D graphics2D, MapContent mapContent, MapViewport mapViewport) {
        double screenX1, screenY1;
        double screenX2, screenY2;

        Point2D.Double point1 = worldToScreen(new Point2D.Double(x1, y1), mapContent, mapViewport);
        Point2D.Double point2 = worldToScreen(new Point2D.Double(x2, y2), mapContent, mapViewport);
        screenX1 = point1.getX();
        screenY1 = point1.getY();
        screenX2 = point2.getX();
        screenY2 = point2.getY();

        graphics2D.setStroke(new BasicStroke(strokeWidth));
        graphics2D.setColor(color);
        graphics2D.draw(new Rectangle.Double(screenX1, screenY1, (screenX2-screenX1), (screenY2-screenY1)));
    }

    public Color getColor() {
        return color;
    }

    public double[] getCoordinates() {
        return new double[] { x1, y1, x2, y2};
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void displayAnnotation() {

    }
}
