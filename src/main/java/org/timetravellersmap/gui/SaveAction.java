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

import org.geotools.swing.MapPane;
import org.geotools.swing.action.MapAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * SaveAction: button action for re-saving currently open project to file
 */
public class SaveAction extends MapAction {
    private MapFrame mapFrame;

    public SaveAction(MapPane mapPane, MapFrame mapFrame) {
        super.init(mapPane, "Save", "Save to file", "/save.png");
        this.mapFrame = mapFrame;
    }


    public void actionPerformed(ActionEvent e) {
        mapFrame.saveStateToJson();
    }
}
