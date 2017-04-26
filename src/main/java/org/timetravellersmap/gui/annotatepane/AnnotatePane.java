package org.timetravellersmap.gui.annotatepane;

import org.timetravellersmap.core.event.EventSelectChangeListener;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.overlay.*;
import org.timetravellersmap.core.event.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * AnnotatePane: pane to display annotations for selected event and allow adding, removal and editing of annotations
 */
public class AnnotatePane extends JPanel implements EventSelectChangeListener, LayerChangeListener, LayerComponentChangeListener {
    private final static Logger LOGGER = Logger.getLogger(AnnotatePane.class.getName());
    private AnnotateMenu annotateMenu;
    private JButton addAnnotationButton = new JButton("Add...");
    private JButton editAnnotationButton = new JButton("Edit...");
    private JButton removeAnnotationButton = new JButton("Remove");
    private JComboBox<Layer> layerSelectCombo = new JComboBox<>();
    private JTable annotationTable = new JTable();
    private JScrollPane annotationTableContainer = new JScrollPane(annotationTable);

    private DefaultComboBoxModel<Layer> layerComboBoxModel;

    private MapFrame mapFrame;

    private boolean visible = false;

    private Event selectedEvent = null;

    private ArrayList<LayerComponentChangeListener> layerComponentChangeListeners = new ArrayList<>();

    public AnnotatePane(MapFrame parentMapFrame) {
        addLayerComponentChangeListener(parentMapFrame.getEventPane());
        this.annotateMenu = new AnnotateMenu(parentMapFrame, this);
        this.mapFrame = parentMapFrame;
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // Handle spawning of AnnotateMenu when "Add" button clicked
        addAnnotationButton.addActionListener(actionEvent ->  {
            if (selectedEvent != null) {
                LOGGER.info("Add clicked, Spawn AnnotateMenu (selectedEvent="+selectedEvent+")");
                JButton btn = (JButton) actionEvent.getSource();
                // Spawn popup annotate menu underneath button
                int x = btn.getX();
                int y = btn.getY() + btn.getHeight();
                annotateMenu.show(this, x, y);
            }
        });

        editAnnotationButton.addActionListener(actionEvent -> {
            if (selectedEvent != null) {
                LOGGER.info("Add clicked, Spawn AnnotateMenu (selectedEvent="+selectedEvent+")");
//                JButton btn = (JButton) actionEvent.getSource();
//                // Spawn popup annotate menu underneath button
//                int x = btn.getX();
//                int y = btn.getY() + btn.getHeight();
//                annotateMenu.show(this, x, y);
                LayerComponent selectedAnnotation = getSelectedAnnotation();
                if (selectedAnnotation instanceof PointComponent) {
                    new AddPoint(mapFrame, this, getSelectedEvent(), (PointComponent)selectedAnnotation).setVisible(true);
                }
                else if (selectedAnnotation instanceof RectangleComponent) {
                    new AddRectangle(mapFrame, this, getSelectedEvent(), (RectangleComponent)selectedAnnotation).setVisible(true);
                }
                else {
                    LOGGER.warning("Unrecognised LayerComponent " + selectedAnnotation);
                }
            }
        });

        // Handle removal of an annotation when "Remove" button clicked
        removeAnnotationButton.addActionListener(actionEvent -> {
            LOGGER.info("Remove clicked, removing annotation (selectedEvent="+selectedEvent+")");
//            selectedEvent.getLayer().removeComponent(getSelectedAnnotation(), selectedEvent);
            selectedEvent.removeLayerComponent(getSelectedAnnotation());
            annotationsChanged();
        });

        // Handle changes to layerSelectCombo, updating the event's layer accordingly
        layerSelectCombo.addItemListener(itemEvent -> {
            // We only care about selection events, not deselection events (prevent double firing)
            if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }
            Layer selectedLayer = (Layer)itemEvent.getItem();
            if (selectedEvent != null) {
                LOGGER.info("Changing event " + selectedEvent + " to layer " + selectedLayer);
//                mapFrame.getLayerList().moveEventToLayer(selectedEvent, selectedLayer);
                mapFrame.getLayerList().moveEventToLayer(selectedEvent, selectedLayer.getName());
            }
        });

        // Render the name of the layer in the dropdown by invoking layer.getName()
        layerSelectCombo.setRenderer(new ListCellRenderer<Layer>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Layer> jList, Layer layer, int i, boolean b, boolean b1) {
                DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();
                JLabel renderer = (JLabel) defaultListCellRenderer.getListCellRendererComponent(jList, layer.getName(), i, b, b1);
                return renderer;
            }
        });

        // Begin layout of GUI components
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
        this.add(editAnnotationButton, gc);

        gc.gridx = 2;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        this.add(removeAnnotationButton, gc);

        gc.gridx = 3;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        add(new JLabel("Event layer"), gc);

        gc.gridx = 4;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        add(layerSelectCombo, gc);
//        layerComboBoxModel = new LayerComboBoxModel(mapFrame.getLayerList());
        layerComboBoxModel = new DefaultComboBoxModel<Layer>(mapFrame.getLayerList().getLayers());
        layerSelectCombo.setModel(layerComboBoxModel);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 0.5;
        gc.gridwidth = 4;
        gc.weighty = 0.9;
        gc.ipady = 0;
        this.add(annotationTableContainer, gc);

        annotationTable.setModel(new AnnotationTableModel(selectedEvent));

        annotationTable.setFillsViewportHeight(true);
        annotationTableContainer.setMinimumSize(new Dimension(300, 100));
        // End layout of GUI components

        annotationTable.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                LOGGER.info("mouseMoved " + mouseEvent);
                Point point = mouseEvent.getPoint();
                int row = annotationTable.rowAtPoint(point);
                LOGGER.info("row: " + row);
                if (row >= 0) {
                    LayerComponent layerComponent = selectedEvent.getLayerComponents().get(row);
                    annotationTable.setToolTipText(layerComponent.getDescriptor().getTooltipText());
                }
                else {
                    annotationTable.setToolTipText(null);
                }
            }
        });

    }

    // Helper to toggle visibility state, returning the new isVisible state
    public boolean toggleVisibleState() {
        setVisibilityState(!visible);
        return visible;
    }

    // Change the visibility state of AnnotatePane and adjust parent MapFrame accordingly
   public void setVisibilityState(boolean visible) {
       LOGGER.info("AnnotatePane.visible="+visible);
       this.visible = visible;
       setVisible(visible);
       mapFrame.changeAnnotateDividerState(visible);
   }

   // Handle event deselection events, i.e. in EventPane
    public void eventDeselected() {
        selectedEvent = null;
        annotationTable.clearSelection();
        setVisibilityState(false);
    }

    // Handle event selection events, i.e. in EventPane
    public void eventSelected(Event event) {
        if (event != null) {
            selectedEvent = event;
//            Layer layer = mapFrame.getLayerList().getLayer(new Layer(event.getLayerName()));
            Layer layer = new Layer(event.getLayerName());
            layerSelectCombo.setSelectedIndex(mapFrame.getLayerList().getLayerPosition(layer));
            annotationsChanged();
        }
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    private LayerComponent getSelectedAnnotation() {
        int layerComponentReference = annotationTable.getSelectedRow();
        if (layerComponentReference != -1) {
//            return selectedEvent.getLayer().getEventLayerComponents(selectedEvent).get(layerComponentReference);
            return selectedEvent.getLayerComponents().get(layerComponentReference);
        }
        else {
            return null;
        }
    }

    // Method to be called when annotations are changed externally, i.e. they are added by AddComponent
    public void annotationsChanged() {
        annotationTable.clearSelection();
        annotationTable.setModel(new AnnotationTableModel(selectedEvent));
    }


    public boolean isVisible() {
        return visible;
    }

    // Handle layer change events, i.e. from LayerManager
    public void layerChanged() {
        layerSelectCombo.setModel(new DefaultComboBoxModel<Layer>(mapFrame.getLayerList().getLayers()));
    }

    public void addLayerComponentChangeListener(LayerComponentChangeListener changeListener) {
        layerComponentChangeListeners.add(changeListener);
    }

    public void removeLayerComponentChangeListener(LayerComponentChangeListener changeListener) {
        layerComponentChangeListeners.remove(changeListener);
    }

    public ArrayList<LayerComponentChangeListener> getLayerComponentChangeListeners() {
        return layerComponentChangeListeners;
    }

    private void fireLayerComponentChangeListenerChanged() {
        for (LayerComponentChangeListener changeListener: layerComponentChangeListeners) {
            changeListener.layerComponentChanged();
        }
    }

    public void layerComponentChanged() {
        annotationsChanged();
        fireLayerComponentChangeListenerChanged();
    }
}
