package org.timetravellersmap.gui.annotatepane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 20/03/17.
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
