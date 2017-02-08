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
import org.timetravellersmap.core.Annotation;
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

    // Set coordinate system to WGS84 (the most commonly used and that used by GPS)
    private CoordinateReferenceSystem coordinateReferenceSystem = DefaultGeographicCRS.WGS84;

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
        mapContent.setTitle("Time Traveller's Map");
//        mapContent.getViewport().setCoordinateReferenceSystem(coordinateReferenceSystem);

        // Setup coordinate reference system (crs) and ensure initial view covers entire world
        mapContent.getViewport().setBounds(new ReferencedEnvelope(
                -180.0,
                180.0,
                -90.0,
                90.0,
                coordinateReferenceSystem
        ));
//        mapContent.getViewport().setMatchingAspectRatio(false);

        style = SLD.createSimpleStyle(featureSource.getSchema());
        layer = new FeatureLayer(featureSource, style);
        mapContent.addLayer(layer);

        // Testing of custom layering system
//        LayerList layerList = new LayerList(layer);
//        org.timetravellersmap.overlay.Layer layer1 = new org.timetravellersmap.overlay.Layer("layer1");
        org.timetravellersmap.overlay.Layer defaultLayer = LayerList.DEFAULT_LAYER;
//        LayerComponent layerComponent1 = new RectangleComponent(-100, 100, 200, 0);
        Event wwii = new Event(
                new GregorianCalendar(1939, 0, 1),
                new GregorianCalendar(1945, 0, 1),
                new Annotation("WWII")
        );
        LayerComponent layerComponent1 = new PointComponent(20, 20, 5);
        LayerComponent layerComponent2 = new PointComponent(151, -33, 5);
        defaultLayer.addComponent(layerComponent1, wwii);
        defaultLayer.addComponent(layerComponent2, wwii);
        mapContent.addLayer(defaultLayer);
        ArrayList<Event> eventsToDraw = new ArrayList<>();
        eventsToDraw.add(wwii);
        defaultLayer.setEventsToDraw(eventsToDraw);
//        layerList.updateMapContent(mapContent);
//        layerList.removeLayer(defaultLayer);
    }

    public void show() {
        // Display the map
//        JMapFrame.showMap(mapContent);
        MapFrame.showMap(mapContent);
    }
}
