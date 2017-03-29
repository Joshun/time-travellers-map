package org.timetravellersmap.gui;

import org.timetravellersmap.core.Basemap;
import org.timetravellersmap.core.event.EventChangeListener;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 28/03/17.
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
