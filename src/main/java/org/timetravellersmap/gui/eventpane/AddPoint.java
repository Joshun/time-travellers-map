package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.PointComponent;
import org.timetravellersmap.timeline.Event;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 31/01/17.
 */
public class AddPoint extends AddComponent {
//    private JPanel panel = new JPanel();

    private JTextField longitudeEntry = new JTextField(10);
    private JTextField latitudeEntry = new JTextField(10);
    private JSpinner radiusEntry = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));

//    private JButton addPointButton = new JButton("Add point");
//    private JButton cancelButton = new JButton("Cancel");

//    private LayerManager layerManager;

    private NameAndDescriptionInput nameAndDescriptionInput = new NameAndDescriptionInput(this);

//    private MapFrame mapFrame;
//    private AnnotatePane annotatePane;
    private Event event;

    public AddPoint(AnnotatePane annotatePane, Event event) {
        super(annotatePane, event);
//        setUpPanel();
        this.annotatePane = annotatePane;
        this.event = annotatePane.getSelectedEvent();
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
        panel.add(radiusEntry, gc);

//        gc.gridx = 0;
//        gc.gridy = 3;
//        gc.weighty = 0.2;
//        panel.add(addPointButton, gc);
//
//        gc.gridx = 1;
//        gc.gridy = 3;
//        gc.weighty = 0.2;
//        panel.add(cancelButton, gc);
//
//        gc.gridx = 0;
//        gc.gridy = 4;
//        gc.gridwidth = 2;
//        gc.weighty = 0.2;
//        panel.add(nameAndDescriptionInput, gc);


        setTitle("Add new point");
//        this.add(panel);
        pack();

        longitudeEntry.setText(String.valueOf(0));
        latitudeEntry.setText(String.valueOf(0));

//        addPointButton.addActionListener(actionEvent -> {
//            System.out.println("TODO: add point");
//            annotatePane.annotateEvent(event, createPointComponent());
//        });
//
//        cancelButton.addActionListener(actionEvent -> {
//            this.dispose();
//        });
//
    }

    @Override
    protected LayerComponent createLayerComponent() {
//        event.getLayer().addComponent(createPointComponent(), event);
        return createPointComponent();
    }


    private PointComponent createPointComponent() {
        return new PointComponent(Double.valueOf(longitudeEntry.getText()), Double.valueOf(latitudeEntry.getText()), Double.valueOf((int)radiusEntry.getValue()));
    }
    public static void main(String[] args) {
//        new AddPoint().setVisible(true);
    }
}
