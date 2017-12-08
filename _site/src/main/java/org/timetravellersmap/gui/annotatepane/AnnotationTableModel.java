/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.gui.annotatepane;

import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.PointComponent;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.overlay.RectangleComponent;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

/**
 * AnnotationTableModel: model for displaying annotations in a JTable
 */
public class AnnotationTableModel implements TableModel {
    private String[] layerComponentTableColumns = {"#", "Name", "Type"};
    private ArrayList<LayerComponent> layerComponents = new ArrayList<>();

    public AnnotationTableModel(Event event) {
        if (event != null) {
            loadEventLayerComponents(event);
        }
    }

    public void loadEventLayerComponents(Event event) {
//        ArrayList<LayerComponent> newEventLayerComponents = event.getLayer().getEventLayerComponents(event);
        ArrayList<LayerComponent> newEventLayerComponents = event.getLayerComponents();
        if (newEventLayerComponents != null) {
//            layerComponents = event.getLayer().getEventLayerComponents(event);
            layerComponents = event.getLayerComponents();
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
        LayerComponent layerComponent = layerComponents.get(i);
        if (i1==0) {
            return String.valueOf(i);
        }
        else if (i1==1) {
            return layerComponent.getDescriptor().getName();
        }
        else if (i1==2) {
            Class layerComponentClass = layerComponent.getClass();
            if (layerComponentClass == PointComponent.class) {
                return "Point";
            }
            else if (layerComponentClass == RectangleComponent.class) {
                return "Rectangle";
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
