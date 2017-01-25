package org.timetravellersmap.overlay;

import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;

import java.awt.*;

/**
 * Created by joshua on 23/01/17.
 */
public class RectangleComponent extends LayerComponent {
    // Bounding box coordinates
    // x1,y1: topleft
    // x2,y2: bottomright
    int x1, y1;
    int x2, y2;

    public RectangleComponent(int x1, int y1, int x2, int y2) {
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
        System.out.println(this.toString() + " draw called");
        graphics2D.draw(new Rectangle(x1, y1, (x2-x1), (y2-y1)));
    }


    public void displayAnnotation() {

    }
}
