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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.core.event.EventIndex;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

/**
 * Created by joshua on 13/03/17.
 */
public class EventIndexTest {
    private EventIndex eventIndex;
    @Before
    public void setUp() {
        eventIndex = new EventIndex();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEmpty() {
        ArrayList<Event> events = eventIndex.getPointerEvents(1950);
        assertEquals(events.size(), 0);
    }

    @Test
    public void testAddEvent() {
        Event newEvent = new Event(
                new GregorianCalendar(1939, 8, 1),
                new GregorianCalendar(1945, 8, 2),
                new Descriptor("WWII", "Second World War")
        );
        eventIndex.addEvent(newEvent);

        // Testing start events on given year
        ArrayList<Event> startYearEvents = eventIndex.getStartEventsForYear(1939);
        assertEquals(startYearEvents.size(), 1);

        // Testing leftmost bound case
        ArrayList<Event> eventsLeftBound = eventIndex.getPointerEvents(1939);
        assertEquals(eventsLeftBound.size(), 1);

        // Testing rightmost bound case
        ArrayList<Event> eventsRightBound = eventIndex.getPointerEvents(1945);
        assertEquals(eventsRightBound.size(), 1);

        // Testing centre case
        ArrayList<Event> eventsCentre = eventIndex.getPointerEvents(1942);
        assertEquals(eventsCentre.size(), 1);

        // Testing start counts
        int startCountPrev = eventIndex.countStartEventsForYear(1938);
        int startCount = eventIndex.countStartEventsForYear(1939);
        int startCountNext = eventIndex.countStartEventsForYear(1940);
        assertEquals(startCountPrev, 0);
        assertEquals(startCount, 1);
        assertEquals(startCountNext, 0);

        // Testing end counts
        int endCountPrev = eventIndex.countEndEventsForYear(1944);
        int endCount = eventIndex.countEndEventsForYear(1945);
        int endCountNext = eventIndex.countEndEventsForYear(1946);
        assertEquals(endCountPrev, 0);
        assertEquals(endCount, 1);
        assertEquals(endCountNext, 0);
    }

    @Test
    public void testRemoveEvent() {
        Event newEvent = new Event(
                new GregorianCalendar(1939, 8, 1),
                new GregorianCalendar(1945, 8, 2),
                new Descriptor("WWII", "Second World War")
        );
        eventIndex.addEvent(newEvent);

        ArrayList<Event> addedList = eventIndex.getStartEventsForYear(1939);
        assertEquals(addedList.size(), 1);

        ArrayList<Event> addedList2 = eventIndex.getPointerEvents(1939);
        assertEquals(addedList2.size(), 1);;

        eventIndex.removeEvent(newEvent);
        ArrayList<Event> removedList = eventIndex.getStartEventsForYear(1939);
        assertEquals(removedList.size(), 0);

        ArrayList<Event> removedList2 = eventIndex.getPointerEvents(1939);
        assertEquals(removedList2.size(), 0);

        int startCount = eventIndex.countStartEventsForYear(1939);
        assertEquals(startCount, 0);

        int endCount = eventIndex.countEndEventsForYear(1945);
        assertEquals(endCount, 0);
    }

    @Test
    public void testUpdateEvent() {
        Event oldEvent = new Event(
                new GregorianCalendar(2009, 0, 20),
                new GregorianCalendar(2012, 0, 20),
                new Descriptor("Barack Obama's First Term")
        );

        Event newEvent = new Event(
                new GregorianCalendar(2012, 0, 20),
                new GregorianCalendar(2017, 0, 20),
                new Descriptor("Barack Obama's Second Term")
        );

        // Adding event
        eventIndex.addEvent(oldEvent);
        ArrayList<Event> events = eventIndex.getStartEventsForYear(2009);
        assertEquals(events.size(), 1);
        assertEquals(events.get(0), oldEvent);

        // Updating event
        eventIndex.updateEvent(oldEvent, newEvent);
        ArrayList<Event> updatedEvents1 = eventIndex.getStartEventsForYear(2009);
        assertEquals(updatedEvents1.size(), 0);

        ArrayList<Event> updatedEvents2 = eventIndex.getStartEventsForYear(2012);
        assertEquals(updatedEvents2.size(), 1);
        assertEquals(updatedEvents2.get(0), newEvent);
    }

    @Test
    public void testEventSummary() {
        Event newEvent = new Event(
                new GregorianCalendar(1939, 8, 1),
                new GregorianCalendar(1945, 8, 2),
                new Descriptor("WWII", "Second World War")
        );

        String summaryNoEvents = eventIndex.generateStartEventSummary(1939);
        assertEquals(summaryNoEvents, null);

        eventIndex.addEvent(newEvent);
        String summary = eventIndex.generateStartEventSummary(1939);
        String correctSummary = "<html><b>" + newEvent.getStartDateAsYear() + "</b><br>" + newEvent.getEventDescriptor().getName() + "<br></html>";
        assertEquals(summary, correctSummary);
    }

}
