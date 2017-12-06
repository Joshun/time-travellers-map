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

import org.timetravellersmap.overlay.LayerList;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * LayerTableModel: TableModel to display list of layers, i.e. for the LayerManager
 */
public class LayerTableModel implements TableModel {
    private String[] layerTableColumns = {"Index", "Name"};
    private LayerList layerList;

    public LayerTableModel(LayerList layerList) {
        super();
        this.layerList = layerList;
    }

    @Override
    public int getRowCount() {
        return this.layerList.getCount();
    }

    @Override
    public int getColumnCount() {
        return layerTableColumns.length;
    }

    @Override
    public String getColumnName(int i) {
        return layerTableColumns[i];
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return false;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        if (i1 == 0) {
            return i;
        }
        else {
            return layerList.getLayerAtPosition(i).getName();
        }
    }

    @Override
    public void setValueAt(Object o, int i, int i1) {

    }

    @Override
    public void addTableModelListener(TableModelListener tableModelListener) {

    }

    @Override
    public void removeTableModelListener(TableModelListener tableModelListener) {

    }
}
