package org.timetravellersmap;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

import java.io.File;


/**
 * Created by joshua on 23/01/17.
 */
public class Toplevel {
    // Shapefile and its parsed data
    private File shapeFile;
    private FileDataStore shapeFileStore;
    private SimpleFeatureSource featureSource;

    // Map widget configuration
    private MapContent mapContent;
    private Style style;
    private Layer layer;

    public Toplevel() throws ShapefileException {
        shapeFile = JFileDataStoreChooser.showOpenFile("shp", null);
        if (shapeFile == null) {
            throw new ShapefileException("Failed to parse shapefile");
        }

        try {
            shapeFileStore = FileDataStoreFinder.getDataStore(shapeFile);
            featureSource = shapeFileStore.getFeatureSource();
        }
        catch (java.io.IOException e) {
            throw new ShapefileException("Failed to parse shapefile", e);
        }

        // Create a map content and add our shapefile to it
        mapContent = new MapContent();
        mapContent.setTitle("Quickstart");

        style = SLD.createSimpleStyle(featureSource.getSchema());
        layer = new FeatureLayer(featureSource, style);
        mapContent.addLayer(layer);
    }

    public void show() {
        // Display the map
        JMapFrame.showMap(mapContent);
    }
}
