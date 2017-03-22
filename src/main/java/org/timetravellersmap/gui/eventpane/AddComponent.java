package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.gui.annotatepane.AnnotatePane;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.overlay.LayerComponentChangeListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * AddComponent: base GUI for adding LayerComponent annotations
 */
public abstract class AddComponent extends JFrame {
    protected MapFrame mapFrame;
    protected AnnotatePane annotatePane;
    protected JPanel panel = new JPanel();
    protected Event event;

    private JButton addButton = new JButton("Add point");
    private JButton cancelButton = new JButton("Cancel");

    protected LayerComponent existingLayerComponent = null;

//    protected abstract void setUpPanel();
    protected abstract LayerComponent createLayerComponent();

    private ArrayList<LayerComponentChangeListener> layerComponentChangeListeners = new ArrayList<>();

    private void addLayerComponent() {
        System.out.println("ADDD");
        event.addLayerComponent(createLayerComponent());
        fireLayerComponentChangeListenersChanged();

    }

    private void updateLayerComponent() {
        System.out.println("UPDATE");
        event.removeLayerComponent(existingLayerComponent);
        event.addLayerComponent(createLayerComponent());
        fireLayerComponentChangeListenersChanged();
    }

    public AddComponent(MapFrame ancestorMapFrame, AnnotatePane annotatePane, Event event, LayerComponent existingLayerComponent) {
        this(ancestorMapFrame, annotatePane, event);
        this.existingLayerComponent = existingLayerComponent;

        // If editing an existing LayerComponent, set button text to "Update point" instead of "Add point"
        // And set title to "Edit point"
        if (existingLayerComponent != null) {
            addButton.setText("Update point");
        }
    }


    public AddComponent(MapFrame ancestorMapFrame, AnnotatePane annotatePane, Event event) {
//        this.layerComponentChangeListeners.addAll(annotatePane.getLayerComponentChangeListeners());
        this.layerComponentChangeListeners.add(annotatePane);
        this.mapFrame = ancestorMapFrame;
        this.annotatePane = annotatePane;
        this.event = event;

        // Begin adding listeners
        addButton.addActionListener(actionEvent -> {
            if (existingLayerComponent != null) {
                updateLayerComponent();
            }
            else {
                addLayerComponent();
            }
            annotatePane.annotationsChanged();
            dispose();
        });

        cancelButton.addActionListener(actionEvent -> dispose());
        // End adding listeners

        // Begin layout of GUI components
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        add(panel, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        add(addButton, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        add(cancelButton, gc);
        // End layout of GUI components

    }

    private void fireLayerComponentChangeListenersChanged() {
        for (LayerComponentChangeListener changeListener: layerComponentChangeListeners) {
            changeListener.layerComponentChanged();
        }
    }
}
