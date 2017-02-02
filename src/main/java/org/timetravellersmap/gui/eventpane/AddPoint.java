package org.timetravellersmap.gui.eventpane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 31/01/17.
 */
public class AddPoint extends JFrame {
    private JPanel panel = new JPanel();

    private JTextField longitudeEntry = new JTextField(10);
    private JTextField latitudeEntry = new JTextField(10);
    private JSpinner diameterEntry = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));

    private JButton addPointButton = new JButton("Add point");
    private JButton cancelButton = new JButton("Cancel");

    private NameAndDescriptionInput nameAndDescriptionInput = new NameAndDescriptionInput(this);

    public AddPoint() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 0.2;
        panel.add(new JLabel("Longitude:"), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.weighty = 0.2;
        panel.add(longitudeEntry, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weighty = 0.2;
        panel.add(new JLabel("Latitude:"), gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.weighty = 0.2;
        panel.add(latitudeEntry, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.weighty = 0.2;
        panel.add(new JLabel("Size:"), gc);

        gc.gridx = 1;
        gc.gridy = 2;
        gc.weighty = 0.2;
        panel.add(diameterEntry, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.weighty = 0.2;
        panel.add(addPointButton, gc);

        gc.gridx = 1;
        gc.gridy = 3;
        gc.weighty = 0.2;
        panel.add(cancelButton, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 2;
        gc.weighty = 0.2;
        panel.add(nameAndDescriptionInput, gc);

        setTitle("Add new point");
        this.add(panel);
        pack();

        longitudeEntry.setText(String.valueOf(0));
        latitudeEntry.setText(String.valueOf(0));

        addPointButton.addActionListener(actionEvent -> {
            System.out.println("TODO: add point");
        });

        cancelButton.addActionListener(actionEvent -> {
            this.dispose();
        });
    }
    public static void main(String[] args) {
        new AddPoint().setVisible(true);
    }
}
