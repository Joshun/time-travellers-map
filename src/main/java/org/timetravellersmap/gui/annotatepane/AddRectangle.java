package org.timetravellersmap.gui.annotatepane;

import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.RectangleComponent;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 25/04/17.
 */
public class AddRectangle extends AddComponent {
    private JTextField topX = new JTextField(5);
    private JTextField topY = new JTextField(5);
    private JTextField bottomX = new JTextField(5);
    private JTextField bottomY = new JTextField(5);

    public AddRectangle(MapFrame ancestorMapFrame, AnnotatePane annotatePane, Event event) {
        this(ancestorMapFrame, annotatePane, event, null);
    }

    public AddRectangle(MapFrame ancestorMapFrame, AnnotatePane annotatePane, Event event, LayerComponent existingLayerComponent) {
        super(ancestorMapFrame, annotatePane, event, existingLayerComponent);

        this.annotatePane = annotatePane;
        this.event = event;
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        panel.add(new JLabel("Top X:"), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        panel.add(topX, gc);

        gc.gridx = 2;
        gc.gridy = 0;
        gc.gridwidth = 1;
        panel.add(new JLabel("Top Y:"), gc);

        gc.gridx = 3;
        gc.gridy = 0;
        gc.gridwidth = 1;
        panel.add(topY, gc);


        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        panel.add(new JLabel("Bottom X:"), gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        panel.add(bottomX, gc);

        gc.gridx = 2;
        gc.gridy = 1;
        gc.gridwidth = 1;
        panel.add(new JLabel("Bottom Y:"), gc);

        gc.gridx = 3;
        gc.gridy = 1;
        gc.gridwidth = 1;
        panel.add(bottomY, gc);

        pack();
    }

    public LayerComponent createLayerComponent() {
        return new RectangleComponent(
                Double.valueOf(topX.getText()),
                Double.valueOf(topY.getText()),
                Double.valueOf(bottomX.getText()),
                Double.valueOf(bottomY.getText()));
    }
}
