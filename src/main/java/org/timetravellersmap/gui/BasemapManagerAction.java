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
