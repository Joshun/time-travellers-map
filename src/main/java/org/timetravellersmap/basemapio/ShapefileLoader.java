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
 * Created by joshua on 08/04/17.
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
