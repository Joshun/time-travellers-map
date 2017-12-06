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

import org.timetravellersmap.core.Basemap;
import org.timetravellersmap.core.event.EventChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * StatusBar: widget for displaying extra useful information
 * Currently this is the current basemap with its period of validity, and the total number of events
 * The current basemap name is displayed in red if it is not valid for the selected region but no
 *  suitable replacement has been found
 */
public class StatusBar extends JPanel implements EventChangeListener, BasemapChangeListener {
    private static final String BASEMAP_STATUS_PREFIX = "Basemap: ";
    private static final String EVENTCOUNT_STATUS_PREFIX = "Total events: ";
    private JLabel basemapStatus = new JLabel(BASEMAP_STATUS_PREFIX);
    private JLabel eventCountStatus = new JLabel(EVENTCOUNT_STATUS_PREFIX);

    private MapFrame mapFrame;

    public StatusBar(MapFrame parentMapFrame, int width, int height) {
        this.mapFrame = parentMapFrame;
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        add(basemapStatus);
        add(Box.createHorizontalGlue());
        add(eventCountStatus);
    }

    public void setEventCountStatus(int count) {
        eventCountStatus.setText(EVENTCOUNT_STATUS_PREFIX + count);
    }

    public void setBasemapStatus(Basemap basemap) {
        setBasemapStatus(basemap, true);
    }

    private void setBasemapExpired() {
        basemapStatus.setToolTipText("Basemap is not valid for selected year");
        basemapStatus.setForeground(new Color(255, 0, 0));
    }

    private void setBasemapValid() {
        basemapStatus.setToolTipText("Basemap OK");
        basemapStatus.setForeground(new Color(0, 0, 0));
    }

    public void setBasemapStatus(Basemap basemap, boolean isValid) {
        if (isValid) {
          setBasemapValid();
        }
        else {
            setBasemapExpired();
        }
        basemapStatus.setText(BASEMAP_STATUS_PREFIX
        + basemap.getMapName()
        + " ("
        + basemap.getValidStartDate()
        + "-"
        + basemap.getValidEndDate()
        + ")");
    }

    public void eventChanged() {
        setEventCountStatus(mapFrame.getEventIndex().getTotalEvents());
    }

    public void basemapChanged(Basemap basemap, boolean isValid) {
        setBasemapStatus(basemap, isValid);
    }

    public void basemapExpired() {
        setBasemapExpired();
    }
}
