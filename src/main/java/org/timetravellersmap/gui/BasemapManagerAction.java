package org.timetravellersmap.gui;

import org.geotools.swing.action.MapAction;
import org.timetravellersmap.core.BasemapList;

import java.awt.event.ActionEvent;

/**
 * Created by joshua on 27/03/17.
 */
public class BasemapManagerAction extends MapAction {
    private MapFrame mapFrame;
    private BasemapList basemapList;

    public BasemapManagerAction(MapFrame mapFrame, BasemapList basemapList) {
        super.init(mapFrame.getMapPane(), "Manage basemaps...","Add or remove basemaps", null);
        this.mapFrame = mapFrame;
        this.basemapList = basemapList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new BasemapManager(mapFrame, basemapList).setVisible(true);
    }
}
