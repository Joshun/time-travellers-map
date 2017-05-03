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
