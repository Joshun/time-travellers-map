package org.timetravellersmap.overlay;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.sql.DBLayerComponent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.Rectangle;

/**
 * LayerComponent: represents components to be drawn on top of the map
 */
//@DatabaseTable(tableName = "layercomponents")
public abstract class LayerComponent {
    @DatabaseField(foreign = true)
    private Descriptor layerDescriptor;
    public abstract void draw(Graphics2D graphics2D, MapContent mapContent, MapViewport mapViewport);
    public abstract void displayAnnotation();

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Event event;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private DBLayerComponent dbLayerComponent;

    protected Point2D.Double worldToScreen(Point2D.Double point, MapContent mapContent, MapViewport mapViewport) {
        AffineTransform worldToScreenTransform = mapViewport.getWorldToScreen();
        Point2D.Double result = new Point2D.Double();
        worldToScreenTransform.transform(point, result);
        return result;
    }

    protected double computeMapScale(MapViewport mapViewport) {
        Rectangle screenArea = mapViewport.getScreenArea();
        ReferencedEnvelope bounds = mapViewport.getBounds();
        double scaleX = screenArea.getWidth() / bounds.getWidth();
        return scaleX;
    }

}
