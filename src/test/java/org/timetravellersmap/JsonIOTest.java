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
import org.timetravellersmap.core.BasemapList;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.core.event.EventIndex;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.gui.SettingsState;
import org.timetravellersmap.jsonio.JsonIO;
import org.timetravellersmap.jsonio.JsonIOObject;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerList;

import java.io.File;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Created by joshua on 17/03/17.
 */
public class JsonIOTest {
    private JsonIO jsonIO;
    private File jsonFile;

    private MapContent mapContent;
    private MapFrame mapFrame;
    private Layer baseLayer;
    private LayerList layerList;
    private EventIndex eventIndex;
    private BasemapList basemapList;

    @Before
    public void setUp() {
        try {
            mapContent = new MapContent();
            baseLayer = new Layer("test");
//            mapFrame = new MapFrame(mapContent, baseLayer);
            mapFrame = new MapFrame(mapContent, new SettingsState());
            layerList = new LayerList(mapContent, baseLayer);
            basemapList = new BasemapList();
        }
        catch (TimeTravellersMapException e) {

        }
        eventIndex = new EventIndex();

        jsonIO = new JsonIO();
        jsonFile = new File("ttm_state_test_" + String.valueOf(Math.random()) + ".json");
        System.out.println("Using file " + jsonFile.getName() + " for testing");
    }

    @Test
    public void testSave() {
        Event sampleEvent = new Event(
                new GregorianCalendar(1990, 0, 1),
                new GregorianCalendar(1992, 0, 1),
                new Descriptor("Test Event", "An event for testing")
            );
        eventIndex.addEvent(sampleEvent);
        JsonIOObject jsonIOObject = new JsonIOObject(layerList, eventIndex, basemapList, 1950);
        jsonIO.saveJson(jsonFile, jsonIOObject);
    }

    @Test
    public void testLoad() {
        Event sampleEvent = new Event(
                new GregorianCalendar(1990, 0, 1),
                new GregorianCalendar(1992, 0, 1),
                new Descriptor("Test Event", "An event for testing")
        );
        eventIndex.addEvent(sampleEvent);
        JsonIOObject jsonIOObject = new JsonIOObject(layerList, eventIndex, basemapList, 1950);
        jsonIO.saveJson(jsonFile, jsonIOObject);

        JsonIO readingJsonIO = new JsonIO();
        JsonIOObject readingJsonIOObject = readingJsonIO.loadJson(jsonFile);
        assertEquals(readingJsonIOObject.getEventIndex().getPointerEvents(sampleEvent.getStartDateAsYear()).size(), 1);
    }

    @After
    public void tearDown() {
        System.out.println("Cleaning up " + jsonFile.getName());
        jsonFile.delete();
    }
}
