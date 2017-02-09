package org.timetravellersmap.gui.annotatepane;

import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.PointComponent;
import org.timetravellersmap.core.event.Event;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

/**
 * Created by joshua on 07/02/17.
 */
public class AnnotationTableModel implements TableModel {
    private String[] layerComponentTableColumns = {"Index", "Type"};
    private ArrayList<LayerComponent> layerComponents = new ArrayList<>();

    public AnnotationTableModel(Event event) {
        if (event != null) {
            loadEventLayerComponents(event);
        }
    }

    public void loadEventLayerComponents(Event event) {
        ArrayList<LayerComponent> newEventLayerComponents = event.getLayer().getEventLayerComponents(event);
        if (newEventLayerComponents != null) {
            layerComponents = event.getLayer().getEventLayerComponents(event);
        }
    }

//    public void clearEventLayerComponents() {
//        System.out.println("CLEAR");
//        layerComponents.clear();
//    }

    @Override
    public int getRowCount() {
        return layerComponents.size();
    }

    @Override
    public int getColumnCount() {
        return layerComponentTableColumns.length;
    }

    @Override
    public String getColumnName(int i) {
        return layerComponentTableColumns[i];
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
        if (i1==0) {
            return i;
        }
        else if (i1==1) {
            LayerComponent layerComponent = layerComponents.get(i);
            if (layerComponent.getClass() == PointComponent.class) {
                return "Point";
            }
            else {
                return "Other";
            }
        }
        return null;
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
