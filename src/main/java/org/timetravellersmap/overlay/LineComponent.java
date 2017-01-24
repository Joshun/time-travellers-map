package org.timetravellersmap.overlay;

/**
 * Created by joshua on 24/01/17.
 */
public class LineComponent extends LayerComponent{
    private int x;
    private int y;

    public LineComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "LineComponent (" + this.x + "," + this.y + ")";
    }

    public boolean equals(LineComponent other) {
        return this.x == other.x && this.y == other.y;
    }

    public void draw() {
        System.out.println("Drawing " + this.toString());
    }
}
