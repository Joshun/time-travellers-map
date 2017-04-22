package org.timetravellersmap.gui;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.data.JFileDataStoreChooser;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.timetravellersmap.TimeTravellersMapException;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.ShapefileException;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.LayerList;
import org.timetravellersmap.overlay.PointComponent;
import org.timetravellersmap.core.event.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;


/**
 * Toplevel: the class which sets up the GUI components
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

    private MapFrame mapFrame;

    // Set coordinate system to WGS84 (the most commonly used and that used by GPS)
    private CoordinateReferenceSystem coordinateReferenceSystem = DefaultGeographicCRS.WGS84;

    public Toplevel() throws ShapefileException {
        shapeFile = new File("/home/joshua/Documents/Computer Science/Dissertation Project/sample data/ne_50m_admin_0_sovereignty.shp");
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
        mapContent.setTitle("Time Traveller's Map");

        // Setup coordinate reference system (crs) and ensure initial view covers entire world
        mapContent.getViewport().setBounds(new ReferencedEnvelope(
                -180.0,
                180.0,
                -90.0,
                90.0,
                coordinateReferenceSystem
        ));

//        style = SLD.createSimpleStyle(featureSource.getSchema());
//        layer = new FeatureLayer(featureSource, style);

        try {
//            mapFrame = new MapFrame(mapContent, layer);
            mapFrame = new MapFrame(mapContent, new SettingsState());
        }
        catch (TimeTravellersMapException e) {
            System.out.println("Failed to initialise GUI " + e);
            e.printStackTrace();
        }

    }

    public void show() {
        mapFrame.showMap();
    }

    public MapFrame getMapFrame() {
        return mapFrame;
    }
}
