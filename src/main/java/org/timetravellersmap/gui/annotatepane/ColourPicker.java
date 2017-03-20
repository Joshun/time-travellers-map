package org.timetravellersmap.gui.annotatepane;

import javax.swing.*;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import java.awt.*;

/**
 * ColourPicker: a GUI window for selecting annotation colour
 */
public class ColourPicker extends JFrame {
    private JPanel panel = new JPanel();
    private JColorChooser jColorChooser = new JColorChooser(new DefaultColorSelectionModel());
    private JButton okButton = new JButton("OK");
    private JButton cancelButton = new JButton("Cancel");

    public ColourPicker() {
        this(null);
    }

    public ColourPicker(Color existingColour) {
        if (existingColour != null ) {
            jColorChooser.setColor(existingColour);
        }
        okButton.addActionListener(actionEvent -> {
            System.out.println(getSelectedColour() + " was selected.");
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

    public static void main(String[] args) {
        // Test harness
        new ColourPicker(new Color(0, 255, 0)).setVisible(true);
    }
}
