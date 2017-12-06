/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.gui;

import org.geotools.swing.action.MapAction;
import org.timetravellersmap.core.BasemapList;

import java.awt.event.ActionEvent;

/**
 * BasemapManagerAction: button action for displaying the BasemapManager
 */
public class BasemapManagerAction extends MapAction {
    private MapFrame mapFrame;
    private BasemapList basemapList;

    public BasemapManagerAction(MapFrame mapFrame, BasemapList basemapList) {
        super.init(mapFrame.getMapPane(), "Manage basemaps","Add or remove basemaps", "/maps.png");
        this.mapFrame = mapFrame;
        this.basemapList = basemapList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new BasemapManager(mapFrame, basemapList).setVisible(true);
    }
}
