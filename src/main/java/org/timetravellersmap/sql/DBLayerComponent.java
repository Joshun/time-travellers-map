package org.timetravellersmap.sql;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.sun.org.apache.regexp.internal.RE;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.PointComponent;
import org.timetravellersmap.overlay.RectangleComponent;

import java.awt.*;

/**
 * Created by joshua on 02/03/17.
 */
@DatabaseTable(tableName = "dbLayerComponents")
public class DBLayerComponent extends LayerComponent {
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private PointComponent pointComponent = null;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private RectangleComponent rectangleComponent = null;
//    @DatabaseField(generatedId = true)
//    private int id;
//    @DatabaseField(foreign = true, foreignAutoRefresh = true)
//    private Event event;

    public DBLayerComponent(PointComponent pointComponent) {
        this.pointComponent = pointComponent;
    }

    public DBLayerComponent(RectangleComponent rectangleComponent) {
        this.rectangleComponent = rectangleComponent;
    }

    public DBLayerComponent(LayerComponent layerComponent) {
        if (layerComponent instanceof PointComponent) {
            this.pointComponent = (PointComponent) layerComponent;
        }
        else if (layerComponent instanceof RectangleComponent) {
            this.rectangleComponent = (RectangleComponent) layerComponent;
        }
    }

    public void draw(Graphics2D graphics2D, MapContent mapContent, MapViewport mapViewport) {
        if (pointComponent != null) {
            pointComponent.draw(graphics2D, mapContent, mapViewport);
        }
        else if (rectangleComponent != null) {
            rectangleComponent.draw(graphics2D, mapContent, mapViewport);
        }
    }

    public void displayAnnotation() {
    }

}
