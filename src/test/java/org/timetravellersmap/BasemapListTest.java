/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap;

import junit.framework.TestCase;
import org.geotools.map.MapContent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.timetravellersmap.core.Basemap;
import org.timetravellersmap.core.BasemapList;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by joshua on 05/04/17.
 */
public class BasemapListTest {
    private BasemapList basemapList;
    @Before
    public void setUp() {
        basemapList = new BasemapList();
    }

    @Test
    public void testAddBasemap() {
        Basemap basemap = new Basemap("testmap.shp", "Test Map", 1900, 2000);
        basemapList.addBasemap(basemap);

        Basemap foundBasemap = basemapList.getFromPointer(1950);
        assertEquals(foundBasemap, basemap);
    }

    @Test
    public void testRemoveBasemap() {
        Basemap basemap = new Basemap("testmap.shp", "Test Map", 1900, 2000);
        basemapList.addBasemap(basemap);

        Basemap foundBasemap = basemapList.getFromPointer(1950);
        assertEquals(foundBasemap, basemap);

        basemapList.removeBasemap(basemap);
        Basemap foundBasemap2 = basemapList.getFromPointer(1950);
        assertEquals(foundBasemap2, null);
    }

    @Test
    public void testBasemapExpired() {
        Basemap basemap = new Basemap("world.shp", "World Map C18th", 1700, 1800);
        assertEquals(BasemapList.basemapExpired(basemap, 1801), true);
    }

    @Test
    public void testBasemapValid() {
        Basemap basemap = new Basemap("ancient.shp", "World 1st Century", 1, 100);
        assertEquals(BasemapList.basemapExpired(basemap, 50), false);
    }

    @Test
    public void testGenerateFlattened() {
        Basemap basemap1 = new Basemap("world.shp", "World Map C18th", 1700, 1800);
        Basemap basemap2 = new Basemap("ancient.shp", "World 1st Century", 1, 100);
        basemapList.addBasemap(basemap1);
        basemapList.addBasemap(basemap2);

        ArrayList<Basemap> flattened = basemapList.getFlattenedBasemaps();

        assertEquals(flattened.size(), 2);
        assertEquals(flattened.contains(basemap1), true);
        assertEquals(flattened.contains(basemap2), true);
    }

    @After
    public void tearDown() {

    }
}
