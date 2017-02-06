package org.timetravellersmap.gui.eventpane;

import net.miginfocom.layout.Grid;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.timeline.Event;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 06/02/17.
 */
public abstract class AddComponent extends JFrame {
    protected AnnotatePane annotatePane;
    protected JPanel panel = new JPanel();
    protected Event event;

    private JButton addButton = new JButton("Add point");
    private JButton cancelButton = new JButton("Cancel");

//    protected abstract void setUpPanel();
    protected abstract LayerComponent createLayerComponent();

    private void addLayerComponent() {
        event.getLayer().addComponent(createLayerComponent(), event);
    }


    public AddComponent(AnnotatePane annotatePane, Event event) {
        this.annotatePane = annotatePane;
        this.event = event;
//        setUpPanel();
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

        addButton.addActionListener(actionEvent -> {
            addLayerComponent();
            dispose();
        });

        cancelButton.addActionListener(actionEvent -> dispose());
    }
}
