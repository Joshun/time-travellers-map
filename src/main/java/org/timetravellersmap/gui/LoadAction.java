package org.timetravellersmap.gui;

import org.geotools.swing.MapPane;
import org.geotools.swing.action.MapAction;

import java.awt.event.ActionEvent;

/**
 * Created by joshua on 13/03/17.
 */
public class LoadAction extends MapAction {
    private MapFrame mapFrame;
    public LoadAction(MapPane mapPane, MapFrame mapFrame) {
        super.init(mapPane, "Load", "Load from file", null);
        this.mapFrame = mapFrame;
    }

    public void actionPerformed(ActionEvent e) {
        mapFrame.loadStateOnTheFly();
    }
}
