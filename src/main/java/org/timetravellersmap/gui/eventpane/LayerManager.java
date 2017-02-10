package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerList;
import org.timetravellersmap.overlay.LayerChangeListener;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * Created by joshua on 02/02/17.
 */
public class LayerManager extends JFrame {
    private JButton addLayerButton = new JButton("Add");
    private JButton removeLayerButton = new JButton("Remove");
    private JButton moveUpButton = new JButton("Move up");
    private JButton moveDownButton = new JButton("Move down");

    private JTable layerTable = new JTable();
    private LayerTableModel layerTableModel;
    private JScrollPane layerTableContainer = new JScrollPane(layerTable);
    private String[] layerTableColumns = {"Index", "Name"};

    private LayerList layerList;

    private ArrayList<LayerChangeListener> changeListeners = new ArrayList<>();

    private MapFrame mapFrame;

    public LayerManager(LayerList layerList, MapFrame ancestorMapFrame) {
        this.mapFrame = ancestorMapFrame;
        setTitle("Manage Layers");
        mapFrame.setEnabled(false);

        // We want to lock the ancestor MapFrame when the LayerManager is open to prevent issues with comboboxes etc.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // When LayerManager receives close event, re-enable MapFrame and dispose
                mapFrame.setEnabled(true);
                dispose();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });

        this.layerList = layerList;
        layerTableModel = new LayerTableModel(this.layerList);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        addLayerButton.addActionListener(actionEvent -> {
            String layerName = JOptionPane.showInputDialog(this, "Layer name", "New layer", JOptionPane.PLAIN_MESSAGE);
            layerList.addLayer(new Layer(layerName));
//            layerTable.repaint();
            layerTable.setModel(new LayerTableModel(layerList));
            fireChangeListeners();
        });

        removeLayerButton.addActionListener(actionEvent -> {
            Layer layer = getSelectedLayer();
            if (layer != null) {
                layerList.removeLayer(getSelectedLayer());
//                layerTable.repaint();
                layerTable.setModel(new LayerTableModel(layerList));
                layerTable.clearSelection();
                fireChangeListeners();
            }
        });

        moveUpButton.addActionListener(actionEvent -> {
            Layer layer = getSelectedLayer();
            if (layer != null) {
                layerList.moveLayerUp(getSelectedLayer());
                layerTable.repaint();
                layerTable.clearSelection();
            }
        });

        moveDownButton.addActionListener(actionEvent -> {
            Layer layer = getSelectedLayer();
            if (layer != null) {
                layerList.moveLayerDown(getSelectedLayer());
                layerTable.repaint();
                layerTable.clearSelection();
            }
        });

        gc.gridx = 0;
        gc.gridy = 0;
        add(addLayerButton, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        add(removeLayerButton, gc);

        gc.gridx = 2;
        gc.gridy = 0;
        add(moveUpButton, gc);

        gc.gridx = 3;
        gc.gridy = 0;
        add(moveDownButton, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 4;
        add(layerTableContainer, gc);
        layerTable.setFillsViewportHeight(true);

        layerTable.setModel(new LayerTableModel(layerList));

        pack();
    }
    private Layer getSelectedLayer() {
        int layerIndex = layerTable.getSelectedRow();
        if (layerIndex != -1) {
            return layerList.getLayerAtPosition(layerIndex);
        }
        else {
            return null;
        }
    }

    public void addLayerChangeListener(LayerChangeListener layerChangeListener) {
        changeListeners.add(layerChangeListener);
    }

    public void removeLayerChangeListener(LayerChangeListener layerChangeListener) {
        changeListeners.remove(layerChangeListener);
    }

    public void fireChangeListeners() {
        for (LayerChangeListener changeListener: changeListeners) {
            changeListener.layerChanged();
        }
    }
}
