/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

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
