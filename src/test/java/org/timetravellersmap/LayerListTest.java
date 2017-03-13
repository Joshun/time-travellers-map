package org.timetravellersmap;

import junit.framework.TestCase;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.*;
import org.geotools.map.Layer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.timetravellersmap.overlay.*;

import java.awt.*;

/**
 * Created by joshua on 13/03/17.
 */
public class LayerListTest extends TestCase {
    private LayerList layerList;

    @Before
    public void setUp() {
        MapContent mapContent = new MapContent();
        org.geotools.map.Layer layer = new Layer() {
            @Override
            public ReferencedEnvelope getBounds() {
                return null;
            }
        };
        layerList = new LayerList(mapContent, layer);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testDefault() {
        assertEquals(layerList.getLayers().length, 1);
    }

    @Test
    public void testAddLayer() {
        org.timetravellersmap.overlay.Layer layer = new org.timetravellersmap.overlay.Layer("Test Layer");
        layerList.addLayer(layer);
        assertEquals(layerList.getLayers().length, 2);
    }

    @Test
    public void testRemoveLayer() {
        org.timetravellersmap.overlay.Layer layer = new org.timetravellersmap.overlay.Layer("Test Layer");
        layerList.addLayer(layer);
        assertEquals(layerList.getLayers().length, 2);

        layerList.removeLayer(layer);
        assertEquals(layerList.getLayers().length, 1);
    }

    public void testMoveLayerUp() {
        org.timetravellersmap.overlay.Layer layer1 = new org.timetravellersmap.overlay.Layer("Layer 1");
        org.timetravellersmap.overlay.Layer layer2 = new org.timetravellersmap.overlay.Layer("Layer 2");
        layerList.addLayer(layer1);
        layerList.addLayer(layer2);

        // Initial ordering
        Layer[] layers = layerList.getLayers();
        assertEquals(layers.length, 3);
        assertEquals(layers[0], LayerList.DEFAULT_LAYER);
        assertEquals(layers[1], layer1);
        assertEquals(layers[2], layer2);

        // Move layer 2 up
        layerList.moveLayerUp(layer2);
        Layer[] movedLayers = layerList.getLayers();
        assertEquals(movedLayers[0], LayerList.DEFAULT_LAYER);
        assertEquals(movedLayers[1], layer2);
        assertEquals(movedLayers[2], layer1);

        // Move default layer down
        layerList.moveLayerDown(LayerList.DEFAULT_LAYER);
        Layer[] movedLayers2 = layerList.getLayers();
        assertEquals(movedLayers2[0], layer2);
        assertEquals(movedLayers2[1], LayerList.DEFAULT_LAYER);
        assertEquals(movedLayers[2], layer1);
    }
}
