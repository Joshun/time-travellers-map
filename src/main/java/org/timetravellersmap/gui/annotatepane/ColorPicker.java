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
import javax.swing.colorchooser.DefaultColorSelectionModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * ColorPicker: a GUI window for selecting annotation colour
 */
public class ColorPicker extends JFrame {
    private final static Logger LOGGER = Logger.getLogger(ColorPicker.class.getName());
    private JPanel panel = new JPanel();
    private JColorChooser jColorChooser = new JColorChooser(new DefaultColorSelectionModel());
    private JButton okButton = new JButton("OK");
    private JButton cancelButton = new JButton("Cancel");
    private Color existingColor;

    private ArrayList<ColorChangeListener> colorChangeListeners = new ArrayList<>();

    public ColorPicker() {
        this(null);
    }

    public ColorPicker(Color existingColour) {
        this.existingColor = existingColour;
        if (existingColour != null ) {
            jColorChooser.setColor(existingColour);
        }
        okButton.addActionListener(actionEvent -> {
//            this.existingColor = getSelectedColour();
            fireColorChangeListeners(jColorChooser.getColor());
            LOGGER.info(getSelectedColour() + " was selected.");
            dispose();
        });

        cancelButton.addActionListener(actionEvent -> {
            dispose();
        });

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        layout.setConstraints(panel, gc);
        panel.setLayout(layout);

        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.gridheight = 1;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        panel.add(jColorChooser, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        panel.add(okButton, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        panel.add(cancelButton, gc);

        setTitle("Pick a colour...");
        add(panel);
        pack();
    }

    public Color getSelectedColour() {
        return jColorChooser.getColor();
    }

    public void addColorChangeListener(ColorChangeListener listener) {
        colorChangeListeners.add(listener);
    }

    public void removeColorChangeListener(ColorChangeListener listener) {
        colorChangeListeners.remove(listener);
    }

    private void fireColorChangeListeners(Color color) {
        for (ColorChangeListener listener: colorChangeListeners) {
            listener.colorChanged(color);
        }
    }

    public static void main(String[] args) {
        // Test harness
        new ColorPicker(new Color(0, 255, 0)).setVisible(true);
    }
}
