package org.timetravellersmap.overlay;

import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * RectangleComponent: represents a bounding box rectangle to draw
 */
public class RectangleComponent extends LayerComponent {
    // Bounding box coordinates
    // x1,y1: topleft
    // x2,y2: bottomright
    private double x1, y1;
    private double x2, y2;

    public RectangleComponent() {
        this(0, 0, 0, 0);
    }

    public RectangleComponent(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
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

//        graphics2D.setStroke(new BasicStroke(40));
        graphics2D.draw(new Rectangle.Double(screenX1, screenY1, (screenX2-screenX1), (screenY2-screenY1)));
    }


    public void displayAnnotation() {

    }
}
