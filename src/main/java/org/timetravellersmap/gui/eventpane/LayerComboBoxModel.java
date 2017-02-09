package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerList;

import javax.swing.*;
import javax.swing.event.ListDataListener;


/**
 * Created by joshua on 03/02/17.
 */
public class LayerComboBoxModel extends DefaultComboBoxModel {
    private LayerList layerList;
    private Layer selectedLayer = null;

    public LayerComboBoxModel(LayerList layerList) {
        super();
        this.layerList = layerList;
    }

//    public void setLayerList(LayerList layerList) {
//        this.layerList = layerList;
//        selectedLayer = null;
//    }
//
//    @Override
//    public void setSelectedItem(Object o) {
//        selectedLayer = (Layer)o;
//    }

//    @Override
//    public Layer getSelectedItem() {
//        return selectedLayer;
//    }

    @Override
    public int getSize() {
        return layerList.getCount();
    }

    @Override
    public Layer getElementAt(int i) {
        return layerList.getLayerAtPosition(i);
    }

    @Override
    public void addListDataListener(ListDataListener listDataListener) {

    }

    @Override
    public void removeListDataListener(ListDataListener listDataListener) {

    }
}


