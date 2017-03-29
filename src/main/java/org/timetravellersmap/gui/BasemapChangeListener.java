package org.timetravellersmap.gui;

import org.timetravellersmap.core.Basemap;

/**
 * Created by joshua on 29/03/17.
 */
public interface BasemapChangeListener {
    public void basemapChanged(Basemap basemap, boolean isValid);
    public void basemapExpired();
}
