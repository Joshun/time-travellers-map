/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.overlay.*;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * LayerManager: a GUI for adding, removing and changing order of layers
 */
public class LayerManager extends JFrame {
    private JPanel panel = new JPanel();
    private JButton addLayerButton = new JButton("Add");
    private JButton removeLayerButton = new JButton("Remove");
    private JButton moveUpButton = new JButton("Move up");
    private JButton moveDownButton = new JButton("Move down");

    private JTable layerTable = new JTable();
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
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // When LayerManager receives close event, re-enable MapFrame and dispose
                mapFrame.setEnabled(true);
                dispose();
            }
        });

        this.layerList = layerList;

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        addLayerButton.addActionListener(actionEvent -> {
            String layerName = JOptionPane.showInputDialog(this, "Layer name", "New layer", JOptionPane.PLAIN_MESSAGE);
            try {
                layerList.addLayer(new Layer(layerName));
                updateTable();
                fireChangeListeners();
            }
            catch (LayerAlreadyExistsException e) {
                JOptionPane.showMessageDialog(this, "Layer \"" + layerName + "\" already exists", "Failed to add layer", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeLayerButton.addActionListener(actionEvent -> {
            Layer layer = getSelectedLayer();
            if (layer != null) {
                try {
                    layerList.removeLayer(getSelectedLayer().getName());
                    layerTable.clearSelection();
                    updateTable();
                    fireChangeListeners();
                }
                catch (AttemptRemoveDefaultLayerException e) {
                    JOptionPane.showMessageDialog(this, "Default layer cannot be removed!", "Failed to remove layer", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        moveUpButton.addActionListener(actionEvent -> {
            Layer layer = getSelectedLayer();
            if (layer != null) {
                layerList.moveLayerUp(getSelectedLayer());
                layerTable.clearSelection();
                updateTable();
            }
        });

        moveDownButton.addActionListener(actionEvent -> {
            Layer layer = getSelectedLayer();
            if (layer != null) {
                layerList.moveLayerDown(getSelectedLayer());
                layerTable.clearSelection();
                updateTable();
            }
        });

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 0.25;
        panel.add(addLayerButton, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        panel.add(removeLayerButton, gc);

        gc.gridx = 2;
        gc.gridy = 0;
        panel.add(moveUpButton, gc);

        gc.gridx = 3;
        gc.gridy = 0;
        panel.add(moveDownButton, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 4;

        layerTableContainer.setMinimumSize(new Dimension(500, 500));

        panel.add(layerTableContainer, gc);
        layerTable.setFillsViewportHeight(true);

        layerTable.setModel(new DefaultTableModel(buildModelList(layerList), layerTableColumns));
        updateTable();

        add(panel);
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

    public void updateTable() {
        layerTable.setModel(new DefaultTableModel(buildModelList(layerList), layerTableColumns));

    }

    public void fireChangeListeners() {
        for (LayerChangeListener changeListener: changeListeners) {
            changeListener.layerChanged();
        }
    }

    public void setModelValues(String[][] values, TableModel tableModel) {
        for (int i=0; i<values.length; i++) {
            tableModel.setValueAt(values[i][0], i, 0);
            tableModel.setValueAt(values[i][1], i, 1);
        }
    }

    public String[][] buildModelList(LayerList layerList) {
        String[][] modelList = new String[layerList.getCount()][2];
        Layer[] layers = layerList.getLayers();
        for (int i=0; i<layerList.getCount(); i++) {
            modelList[i][0] = String.valueOf(i);
            modelList[i][1] = layers[i].getName();
        }
        return modelList;
    }

    public JPanel getPanel() {
        return panel;
    }
}
