package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.timeline.*;
import org.timetravellersmap.timeline.Event;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by joshua on 01/02/17.
 */
public class AnnotatePane extends JPanel implements EventSelectChangeListener {
    private AnnotateMenu annotateMenu;
    private JButton addAnnotationButton = new JButton("Add...");
    private JButton removeAnnotationButton = new JButton("Remove");
    private JComboBox<Layer> layerSelectCombo = new JComboBox<>();
    private JTable annotationTable = new JTable();
    private AnnotationTableModel annotationTableModel = new AnnotationTableModel();
    private JScrollPane annotationTableContainer = new JScrollPane(annotationTable);
    private String[] annotationTableHeadings = {"Type", "Name"};

    private MapFrame mapFrame;

    private boolean visible = false;

    private Event selectedEvent = null;

    public AnnotatePane(MapFrame parentMapFrame) {
        this.annotateMenu = new AnnotateMenu(this);
        this.mapFrame = parentMapFrame;
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        addAnnotationButton.addActionListener(actionEvent ->  {
            System.out.println("Annotate");
            org.timetravellersmap.timeline.Event event = mapFrame.getEventPane().getSelectedEvent();
            if (event != null) {
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

//        gc.gridx = 2;
//        gc.gridy = 0;
//        gc.weightx = 0.5;
//        gc.weighty = 0.1;
//        this.add(manageLayersButton, gc);


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
        layerSelectCombo.setModel(new LayerComboBoxModel(mapFrame.getLayerList()));

//        gc.gridx = 1;
//        gc.gridy = 1;
//        gc.gridwidth = 2;
//        add(layerSelectCombo, gc);
//        layerSelectCombo.setModel(new LayerComboBoxModel(mapFrame.getLayerList()));
//        if (mapFrame.getEventPane().getSelectedEvent() != null) {
//            layerSelectCombo.setSelectedItem(mapFrame.getEventPane().getSelectedEvent().getLayer());
//        }
//        loadEvent();

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

//        annotationTable.setModel(new LayerTableModel(mapFrame.getLayerList()));
        System.out.println(mapFrame.getLayerList().getCount());
    }

    public boolean toggleVisibleState() {
        visible = !visible;
        System.out.println("AnnotatePane.visible="+visible);
//        loadEvent();
        setVisible(visible);
        return visible;
    }

    public void eventDeselected() {
        System.out.println("DESELECT");
//        visible = false;
//        setVisible(false);
        if (visible) {
            mapFrame.toggleAnnotatePane();
            annotationTableModel.clearEventLayerComponents();
        }
    }

    public void eventSelected(Event event) {
//        Event event = mapFrame.getEventPane().getSelectedEvent();
        if (event != null) {
            System.out.println("OK");
            System.out.println(event.getLayer());
            layerSelectCombo.setSelectedItem(event.getLayer());
            selectedEvent = event;
            annotationTableModel.loadEventLayerComponents(event);
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
        annotationTableModel.loadEventLayerComponents(selectedEvent);
        annotationTable.updateUI();
    }

//    public void annotateEvent(Event event, LayerComponent layerComponent) {
//        System.out.println(event);
//        Layer eventLayer = event.getLayer();
//        eventLayer.addComponent(layerComponent, event);
//    }

//    public void loadEvent() {
//        Event event = mapFrame.getEventPane().getSelectedEvent();
//        if (event != null) {
//            System.out.println("OK");
//            System.out.println(event.getLayer());
//            layerSelectCombo.setSelectedItem(event.getLayer());
//        }
//    }

    public boolean isVisible() {
        return visible;
    }
}
