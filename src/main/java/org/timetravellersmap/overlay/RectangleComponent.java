package org.timetravellersmap.overlay;

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

    public void draw() {

    }
}
