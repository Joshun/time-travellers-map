package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.overlay.LayerList;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Created by joshua on 03/02/17.
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
