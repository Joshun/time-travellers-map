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

import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerList;

import javax.swing.*;
import javax.swing.event.ListDataListener;


/**
 * LayerComboBoxModel: used to create dropdown for changing the Layer an Event belongs to
 */
public class LayerComboBoxModel extends DefaultComboBoxModel {
    private LayerList layerList;
    private Layer selectedLayer = null;

    public LayerComboBoxModel(LayerList layerList) {
        super();
        this.layerList = layerList;
    }

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


