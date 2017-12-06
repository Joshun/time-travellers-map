/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.basemapio;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;

import java.io.File;

/**
 * ShapefileLoader: class for loading Shapefiles
 */
public class ShapefileLoader implements BasemapLoader {
    public Layer loadBasemap(File file) throws BasemapIOException {
        try {
            FileDataStore shapeFileStore = FileDataStoreFinder.getDataStore(file);
            SimpleFeatureSource featureSource = shapeFileStore.getFeatureSource();
            Style style = SLD.createSimpleStyle(featureSource.getSchema());
            return new FeatureLayer(featureSource, style);
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
            throw new BasemapIOException("Failed to load " + file.getName());
        }
    }
}
