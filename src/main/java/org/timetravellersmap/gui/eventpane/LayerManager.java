package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.overlay.LayerList;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by joshua on 02/02/17.
 */
public class LayerManager extends JFrame {
    private JButton addLayerButton = new JButton("Add");
    private JButton removeLayerButton = new JButton("Remove");
    private JButton moveUpButton = new JButton("Move up");
    private JButton moveDownButton = new JButton("Move down");

    private JTable layerTable = new JTable();
    private JScrollPane layerTableContainer = new JScrollPane(layerTable);
    private String[] layerTableColumns = {"Index", "Name"};

    private LayerList layerList;

    public LayerManager(LayerList layerList) {
        this.layerList = layerList;
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

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
}
