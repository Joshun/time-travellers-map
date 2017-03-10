package org.timetravellersmap.gui;

import org.geotools.swing.MapPane;
import org.geotools.swing.action.MapAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by joshua on 10/03/17.
 */
public class SaveAction extends MapAction {

    public SaveAction(MapPane mapPane) {
        super.init(mapPane, "Save", "Save to file", null);
    }


    /**
     * Get the map pane that this Action is working with
     *
     * @return the map pane
     */

    public void actionPerformed(ActionEvent e) {
        System.out.println("Save clicked...");
    }
}
