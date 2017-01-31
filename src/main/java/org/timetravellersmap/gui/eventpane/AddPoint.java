package org.timetravellersmap.gui.eventpane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 31/01/17.
 */
public class AddPoint extends JFrame{
    private JPanel panel;
    private JTextField longitudeEntry;
    private JTextField latitudeEntry;
    private JSpinner diameterEntry;

    public AddPoint() {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Longitude:"), gc);

        setTitle("Add new point");
        this.add(panel);
        pack();
    }
}
