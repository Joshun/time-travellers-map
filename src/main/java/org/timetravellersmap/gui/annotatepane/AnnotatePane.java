package org.timetravellersmap.gui.annotatepane;

import org.timetravellersmap.core.event.EventSelectChangeListener;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.gui.eventpane.LayerComboBoxModel;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.core.event.Event;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 01/02/17.
 */
public class AnnotatePane extends JPanel implements EventSelectChangeListener, LayerChangeListener {
    private AnnotateMenu annotateMenu;
    private JButton addAnnotationButton = new JButton("Add...");
    private JButton removeAnnotationButton = new JButton("Remove");
    private JComboBox<Layer> layerSelectCombo = new JComboBox<>();
    private JTable annotationTable = new JTable();
    private AnnotationTableModel annotationTableModel = new AnnotationTableModel();
    private JScrollPane annotationTableContainer = new JScrollPane(annotationTable);
    private String[] annotationTableHeadings = {"Type", "Name"};

    private LayerComboBoxModel layerComboBoxModel;

    private MapFrame mapFrame;

    private boolean visible = false;

    private Event selectedEvent = null;

    public AnnotatePane(MapFrame parentMapFrame) {
        this.annotateMenu = new AnnotateMenu(this);
        this.mapFrame = parentMapFrame;
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        addAnnotationButton.addActionListener(actionEvent ->  {
            if (selectedEvent != null) {
                JButton btn = (JButton) actionEvent.getSource();
                // Spawn popup annotate menu underneath button
                int x = btn.getX();
                int y = btn.getY() + btn.getHeight();
                annotateMenu.show(this, x, y);
            }
        });

        removeAnnotationButton.addActionListener(actionEvent -> {
            selectedEvent.getLayer().removeComponent(getSelectedAnnotation(), selectedEvent);
            annotationsChanged();
        });

        layerSelectCombo.addItemListener(itemEvent -> {
            Layer selectedLayer = (Layer)itemEvent.getItem();
            if (selectedEvent != null) {
                mapFrame.getLayerList().moveEventToLayer(selectedEvent, selectedLayer);
            }
        });

        gc.anchor = GridBagConstraints.PAGE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 1;
        gc.ipady = 0;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        this.add(addAnnotationButton, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        this.add(removeAnnotationButton, gc);

        gc.gridx = 2;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        add(new JLabel("Event layer"), gc);

        gc.gridx = 3;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        add(layerSelectCombo, gc);
        layerComboBoxModel = new LayerComboBoxModel(mapFrame.getLayerList());
        layerSelectCombo.setModel(layerComboBoxModel);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 0.5;
        gc.gridwidth = 4;
        gc.weighty = 0.9;
        gc.ipady = 0;
        this.add(annotationTableContainer, gc);

        annotationTable.setModel(annotationTableModel);

        annotationTable.setFillsViewportHeight(true);
        annotationTableContainer.setMinimumSize(new Dimension(300, 100));

    }

    public boolean toggleVisibleState() {
        setVisibilityState(!visible);
        return visible;
    }

   public void setVisibilityState(boolean visible) {
       System.out.println("AnnotatePane.visible="+visible);
       this.visible = visible;
       setVisible(visible);
       mapFrame.changeAnnotateDividerState(visible);
   }

    public void eventDeselected() {
        selectedEvent = null;
        annotationTable.clearSelection();
        setVisibilityState(false);
    }

    public void eventSelected(Event event) {
        if (event != null) {
            System.out.println(event.getLayer());
            layerSelectCombo.setSelectedItem(event.getLayer());
            selectedEvent = event;
            annotationsChanged();
        }
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    private LayerComponent getSelectedAnnotation() {
        int layerComponentReference = annotationTable.getSelectedRow();
        if (layerComponentReference != -1) {
            return selectedEvent.getLayer().getEventLayerComponents(selectedEvent).get(layerComponentReference);
        }
        else {
            return null;
        }
    }

    public void annotationsChanged() {
        annotationTable.clearSelection();
        annotationTableModel.loadEventLayerComponents(selectedEvent);
//        annotationTable.updateUI();
    }


    public boolean isVisible() {
        return visible;
    }

    public void layerChanged() {
        layerSelectCombo.updateUI();
    }
}
