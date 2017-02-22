package org.timetravellersmap.gui.annotatepane;

import org.timetravellersmap.core.event.EventSelectChangeListener;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.gui.eventpane.LayerComboBoxModel;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.LayerChangeListener;
import org.timetravellersmap.core.event.Event;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * AnnotatePane: pane to display annotations for selected event and allow adding, removal and editing of annotations
 */
public class AnnotatePane extends JPanel implements EventSelectChangeListener, LayerChangeListener {
    private AnnotateMenu annotateMenu;
    private JButton addAnnotationButton = new JButton("Add...");
    private JButton removeAnnotationButton = new JButton("Remove");
    private JComboBox<Layer> layerSelectCombo = new JComboBox<>();
    private JTable annotationTable = new JTable();
    private JScrollPane annotationTableContainer = new JScrollPane(annotationTable);
    private String[] annotationTableHeadings = {"Type", "Name"};

    private DefaultComboBoxModel<Layer> layerComboBoxModel;

    private MapFrame mapFrame;

    private boolean visible = false;

    private Event selectedEvent = null;

    public AnnotatePane(MapFrame parentMapFrame) {
        this.annotateMenu = new AnnotateMenu(this);
        this.mapFrame = parentMapFrame;
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // Handle spawning of AnnotateMenu when "Add" button clicked
        addAnnotationButton.addActionListener(actionEvent ->  {
            if (selectedEvent != null) {
                JButton btn = (JButton) actionEvent.getSource();
                // Spawn popup annotate menu underneath button
                int x = btn.getX();
                int y = btn.getY() + btn.getHeight();
                annotateMenu.show(this, x, y);
            }
        });

        // Handle removal of an annotation when "Remove" button clicked
        removeAnnotationButton.addActionListener(actionEvent -> {
            selectedEvent.getLayer().removeComponent(getSelectedAnnotation(), selectedEvent);
            annotationsChanged();
        });

        // Handle changes to layerSelectCombo, updating the event's layer accordingly
        layerSelectCombo.addItemListener(itemEvent -> {
            // We only care about selection events, not deselection events (prevent double firing)
            if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                return;
            }
            Layer selectedLayer = (Layer)itemEvent.getItem();
            System.out.println(" layer" + selectedLayer + " event " + selectedEvent);
            if (selectedEvent != null) {
                System.out.println("CHANGE LAYER");
                mapFrame.getLayerList().moveEventToLayer(selectedEvent, selectedLayer);
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

    }

    // Helper to toggle visibility state, returning the new isVisible state
    public boolean toggleVisibleState() {
        setVisibilityState(!visible);
        return visible;
    }

    // Change the visibility state of AnnotatePane and adjust parent MapFrame accordingly
   public void setVisibilityState(boolean visible) {
       System.out.println("AnnotatePane.visible="+visible);
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
            layerSelectCombo.setSelectedIndex(mapFrame.getLayerList().getLayerPosition(event.getLayer()));
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
}
