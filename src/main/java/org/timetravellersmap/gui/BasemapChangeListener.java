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

/**
 * BasemapChangeListener: used to listen for changes in the current basemap
 * A different map could be loaded by the user to represent border changes, for example
 */
public interface BasemapChangeListener {
    void basemapChanged(Basemap basemap, boolean isValid);
    void basemapExpired();
}
