package org.timetravellersmap;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.*;
import org.geotools.map.Layer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.timetravellersmap.overlay.*;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Created by joshua on 13/03/17.
 */
public class LayerListTest {
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
        // In a newly created LayerList, there is 1 layer: the default layer (which cannot be removed)
        assertEquals(layerList.getLayers().length, 1);
    }

    @Test
    public void testAddLayer() {
        org.timetravellersmap.overlay.Layer layer = new org.timetravellersmap.overlay.Layer("Test Layer");
        try {
            layerList.addLayer(layer);
        }
        catch (LayerAlreadyExistsException e) {
            fail();
        }
        assertEquals(layerList.getLayers().length, 2);
    }

    @Test
    public void testRemoveLayer() {
        org.timetravellersmap.overlay.Layer layer = new org.timetravellersmap.overlay.Layer("Test Layer");
        try {
            layerList.addLayer(layer);
        }
        catch (LayerAlreadyExistsException e) {
            fail();
        }
        assertEquals(layerList.getLayers().length, 2);

        try {
            layerList.removeLayer(layer.getName());
        }
        catch (AttemptRemoveDefaultLayerException e) {
            fail();
        }
        assertEquals(layerList.getLayers().length, 1);
    }

    @Test
    public void testMoveLayerUp() {
        org.timetravellersmap.overlay.Layer layer1 = new org.timetravellersmap.overlay.Layer("Layer 1");
        org.timetravellersmap.overlay.Layer layer2 = new org.timetravellersmap.overlay.Layer("Layer 2");
        try {
            layerList.addLayer(layer1);
            layerList.addLayer(layer2);
        }
        catch (LayerAlreadyExistsException e) {
            fail();
        }

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
    }

    @Test
    public void moveLayerDown() {
        org.timetravellersmap.overlay.Layer layer1 = new org.timetravellersmap.overlay.Layer("Layer 1");
        org.timetravellersmap.overlay.Layer layer2 = new org.timetravellersmap.overlay.Layer("Layer 2");
        try {
            layerList.addLayer(layer1);
            layerList.addLayer(layer2);
        }
        catch (LayerAlreadyExistsException e) {
            fail();
        }

        // Initial ordering
        Layer[] layers = layerList.getLayers();
        assertEquals(layers.length, 3);
        assertEquals(layers[0], LayerList.DEFAULT_LAYER);
        assertEquals(layers[1], layer1);
        assertEquals(layers[2], layer2);

        // Move layer1 layer down
        layerList.moveLayerDown(layer1);
        Layer[] movedLayers = layerList.getLayers();
        assertEquals(movedLayers[0], LayerList.DEFAULT_LAYER);
        assertEquals(movedLayers[1], layer2);
        assertEquals(movedLayers[2], layer1);
    }
}
