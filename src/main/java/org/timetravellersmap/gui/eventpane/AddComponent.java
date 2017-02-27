package org.timetravellersmap.gui.eventpane;

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
    protected AnnotatePane annotatePane;
    protected JPanel panel = new JPanel();
    protected Event event;

    private JButton addButton = new JButton("Add point");
    private JButton cancelButton = new JButton("Cancel");

//    protected abstract void setUpPanel();
    protected abstract LayerComponent createLayerComponent();

    private ArrayList<LayerComponentChangeListener> layerComponentChangeListeners = new ArrayList<>();

    private void addLayerComponent() {
        System.out.println("ADDD");
//        event.getLayer().addComponent(createLayerComponent(), event);
        event.addLayerComponent(createLayerComponent());
        fireLayerComponentChangeListenersChanged();
//        System.out.println(event.getLayer().getEventLayerComponents(event));

    }


    public AddComponent(AnnotatePane annotatePane, Event event) {
//        this.layerComponentChangeListeners.addAll(annotatePane.getLayerComponentChangeListeners());
        this.layerComponentChangeListeners.add(annotatePane);
        this.annotatePane = annotatePane;
        this.event = event;

        // Begin adding listeners
        addButton.addActionListener(actionEvent -> {
            addLayerComponent();
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
