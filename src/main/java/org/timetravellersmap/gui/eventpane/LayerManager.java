package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerList;
import org.timetravellersmap.overlay.LayerChangeListener;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
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
    private TableModel layerTableModel;
    private JScrollPane layerTableContainer = new JScrollPane(layerTable);
    private String[] layerTableColumns = {"Index", "Name"};

    private LayerList layerList;

    private ArrayList<LayerChangeListener> changeListeners = new ArrayList<>();

    public LayerManager(LayerList layerList) {
        setTitle("Manage Layers");
        this.layerList = layerList;
        layerTableModel = new LayerTableModel(this.layerList);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        addLayerButton.addActionListener(actionEvent -> {
            String layerName = JOptionPane.showInputDialog(this, "Layer name", "New layer", JOptionPane.PLAIN_MESSAGE);
            layerList.addLayer(new Layer(layerName));
            layerTable.updateUI();
            fireChangeListeners();
        });

        removeLayerButton.addActionListener(actionEvent -> {
            Layer layer = getSelectedLayer();
            if (layer != null) {
                layerList.removeLayer(getSelectedLayer());
                layerTable.updateUI();
                layerTable.clearSelection();
                fireChangeListeners();
            }
        });

        moveUpButton.addActionListener(actionEvent -> {
            Layer layer = getSelectedLayer();
            if (layer != null) {
                layerList.moveLayerUp(getSelectedLayer());
                layerTable.updateUI();
                layerTable.clearSelection();
            }
        });

        moveDownButton.addActionListener(actionEvent -> {
            Layer layer = getSelectedLayer();
            if (layer != null) {
                layerList.moveLayerDown(getSelectedLayer());
                layerTable.updateUI();
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

//        layerTable.setModel(new TableModel() {
//            @Override
//            public int getRowCount() {
//                return layerList.getCount();
//            }
//
//            @Override
//            public int getColumnCount() {
//                return layerTableColumns.length;
//            }
//
//            @Override
//            public String getColumnName(int i) {
//                return layerTableColumns[i];
//            }
//
//            @Override
//            public Class<?> getColumnClass(int i) {
//                return String.class;
//            }
//
//            @Override
//            public boolean isCellEditable(int i, int i1) {
//                return false;
//            }
//
//            @Override
//            public Object getValueAt(int i, int i1) {
//                if (i1 == 0) {
//                    return i1;
//                }
//                else {
//                    return layerList.getLayerAtPosition(i).getName();
//                }
//            }
//
//            @Override
//            public void setValueAt(Object o, int i, int i1) {
//
//            }
//
//            @Override
//            public void addTableModelListener(TableModelListener tableModelListener) {
//
//            }
//
//            @Override
//            public void removeTableModelListener(TableModelListener tableModelListener) {
//
//            }
//        });
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
