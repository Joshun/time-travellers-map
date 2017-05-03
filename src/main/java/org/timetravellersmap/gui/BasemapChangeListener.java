package org.timetravellersmap.gui;

import org.timetravellersmap.core.Basemap;

/**
 * BasemapChangeListener: used to listen for changes in the current basemap
 * A different map could be loaded by the user to represent border changes, for example
 */
public interface BasemapChangeListener {
    void basemapChanged(Basemap basemap, boolean isValid);
    void basemapExpired();
}
