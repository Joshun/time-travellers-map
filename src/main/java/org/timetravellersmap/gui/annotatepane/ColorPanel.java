package org.timetravellersmap.gui.annotatepane;

import javax.swing.*;
import java.awt.*;

/**
 * ColorPanel: JPanel configured to display a colour, to show current colour of a LayerComponent
 */
public class ColorPanel extends JPanel implements ColorChangeListener {
    private int width;
    private int height;
    private Color color;

    public ColorPanel(int w, int h, Color c) {
        this.width = w;
        this.height = h;
        this.color = c;
        setPreferredSize(new Dimension(w, h));
    }

    public ColorPanel(int w, int h) {
        this(w, h, new Color(255, 255, 255));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }

    public void colorChanged(Color color) {
        this.color = color;
        repaint();
    }
}
