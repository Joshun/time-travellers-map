package org.timetravellersmap.gui.annotatepane;

import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.RectangleComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by joshua on 25/04/17.
 */
public class AddRectangle extends AddComponent implements ColorChangeListener {
    private JTextField topX = new JTextField(5);
    private JTextField topY = new JTextField(5);
    private JTextField bottomX = new JTextField(5);
    private JTextField bottomY = new JTextField(5);
    private JSpinner strokeWidthEntry = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private ColorPanel colorPanel = new ColorPanel(16, 16, new Color(0, 0, 0));
    private Color colorState = new Color(0, 0, 0);

    public AddRectangle(MapFrame ancestorMapFrame, AnnotatePane annotatePane, Event event) {
        this(ancestorMapFrame, annotatePane, event, null);
    }

    public AddRectangle(MapFrame ancestorMapFrame, AnnotatePane annotatePane, Event event, LayerComponent existingLayerComponent) {
        super(ancestorMapFrame, annotatePane, event, existingLayerComponent);

        AddRectangle that = this;
        colorPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                ColorPicker colorPicker = new ColorPicker(colorState);
                colorPicker.addColorChangeListener(colorPanel);
                colorPicker.addColorChangeListener(that);
                colorPicker.setVisible(true);
            }
        });

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

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        panel.add(new JLabel("Stroke width:"), gc);

        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridwidth = 1;
        panel.add(strokeWidthEntry, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 1;
        panel.add(new JLabel("Colour:"), gc);

        gc.gridx = 1;
        gc.gridy = 3;
        gc.gridwidth = 1;
        panel.add(colorPanel, gc);

        pack();

        if (existingLayerComponent != null) {
            setTitle("Edit rectangle");
            loadExistingRectangleComponent();
        }
        else {
            setTitle("Add new rectangle");
        }
    }

    public void colorChanged(Color color) {
        this.colorState = color;
        colorPanel.repaint();
    }

    public LayerComponent createLayerComponent() {
        return new RectangleComponent(
                Double.valueOf(topX.getText()),
                Double.valueOf(topY.getText()),
                Double.valueOf(bottomX.getText()),
                Double.valueOf(bottomY.getText()),
                colorState,
                Float.valueOf(String.valueOf((int)strokeWidthEntry.getValue())),
                createDescriptor());
    }

    private void loadExistingRectangleComponent() {
        // Check the existingLayerComponent is not null or of another type
        if (existingLayerComponent instanceof RectangleComponent) {
            RectangleComponent existingRectangleComponent = (RectangleComponent) existingLayerComponent;
            colorState = existingRectangleComponent.getColor();
            colorPanel.colorChanged(existingRectangleComponent.getColor());
            strokeWidthEntry.setValue(Integer.valueOf(String.valueOf(existingRectangleComponent.getStrokeWidth())));

            double coordinates[] = existingRectangleComponent.getCoordinates();
            if (coordinates.length == 4) {
                topX.setText(String.valueOf(coordinates[0]));
                topY.setText(String.valueOf(coordinates[1]));
                bottomX.setText(String.valueOf(coordinates[3]));
                bottomY.setText(String.valueOf(coordinates[4]));
            }
        }
    }
}
