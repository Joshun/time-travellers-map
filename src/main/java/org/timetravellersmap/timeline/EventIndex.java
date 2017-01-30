package org.timetravellersmap.timeline;

import org.timetravellersmap.Annotation;

import java.util.*;

/**
 * Created by joshua on 30/01/17.
 */
public class EventIndex {
    private TreeMap<Integer,TreeMap<Integer, ArrayList<Event>>> startYearIndex = new TreeMap<>();

    public EventIndex() {

    }

    public void addEvent(Event event) {
        // Here we need to consider three different cases:
        // Case 1: the startYearIndex does not contain a {startYear,{endYear,eventList}} entry
        //         the whole structure must be created
        // Case 2: the {endYear,eventList} entry for the event's corresponding startYear does not exist
        //         need to add the event to newly created eventList and add this to the endYear tree
        //         for corresponding startYear
        // Case 3: the {startYear,{endYear,eventList}} entry exists but the event is not in the list
        //         simply add the event to the list

        int startYear = event.getStartDateAsYear();
        int endYear = event.getEndDateAsYear();
        TreeMap<Integer, ArrayList<Event>> endYearMap = startYearIndex.get(startYear);
        // Case 1: a {start year, {end year, event list}} set does not exist in startYearIndex
        // Add startYearIndex entry and corresponding {endYear, events} entry
        if (endYearMap == null) {
            endYearMap = new TreeMap<Integer, ArrayList<Event>>();
            ArrayList<Event> eventArrayList = new ArrayList<>();
            eventArrayList.add(event);
            endYearMap.put(endYear, eventArrayList);
            startYearIndex.put(startYear, endYearMap);
        } else {
            ArrayList<Event> eventArrayList = endYearMap.get(endYear);
            // Case 2: a {start year} index exists but does not have a corresponding end year
            // Add {endYear, events} entry
            if (eventArrayList == null) {
                eventArrayList = new ArrayList<>();
                // Add newly created event ArrayList
                endYearMap.put(endYear, eventArrayList);
            }
            // Case 2: add event to newly created event list
            // Case 3: add event to existing ArrayList
            // Add event to eventArrayList
            eventArrayList.add(event);
        }
    }


    public void updateEvent(Event oldEvent, Event newEvent) {
        int startYear = oldEvent.getStartDateAsYear();
        int endYear = oldEvent.getEndDateAsYear();
        TreeMap<Integer, ArrayList<Event>> endYearMap = startYearIndex.get(startYear);
        ArrayList<Event> eventList = endYearMap.get(endYear);
        eventList.remove(oldEvent);

        // Here we are using addEvent since this reinserts it into the tree (the year(s) could have changed)
        addEvent(newEvent);
    }

    public void removeEvent(Event event) {
        int startYear = event.getStartDateAsYear();
        int endYear = event.getEndDateAsYear();
    }

    public ArrayList<Event> getPointerEvents(int pointerYear) {
        ArrayList<Event> allPointerEvents = new ArrayList<>();
        // Get subset of startYearIndex: all nodes with year indexes before or including the pointer year
        NavigableMap<Integer, TreeMap<Integer, ArrayList<Event>>> eventsOnOrBeforePointer = startYearIndex.headMap(pointerYear, true);
        // Loop through the <Year, TreeMap> values
        for (TreeMap<Integer, ArrayList<Event>> endYearMaps: eventsOnOrBeforePointer.values()) {
            // Get subset of <Year, TreeMap> entry with year indexes after or including the pointer year
            NavigableMap<Integer, ArrayList<Event>> eventsOnOrAfterPointer = endYearMaps.tailMap(pointerYear, true);
            // Loop through each of ArrayList<Event> values
            for (ArrayList<Event> eventList: eventsOnOrAfterPointer.values()) {
                allPointerEvents.addAll(eventList);
            }
        }
        return allPointerEvents;
    }

    public void treeWalk(int pointerYear) {
        // Given pointerYear, walk through tree
        // Used for debugging purposes
        ArrayList<Event> allPointerEvents = new ArrayList<>();
        System.out.println("startYearIndex");
        System.out.println(startYearIndex.entrySet());
        // Get subset of startYearIndex: all nodes with year indexes before or including the pointer year
        NavigableMap<Integer, TreeMap<Integer, ArrayList<Event>>> eventsOnOrBeforePointer = startYearIndex.headMap(pointerYear, true);
        // Loop through the <Year, TreeMap> values
        System.out.println("Select on or before pointer date...");
        System.out.println(eventsOnOrBeforePointer.values());
        for (TreeMap<Integer, ArrayList<Event>> endYearMaps: eventsOnOrBeforePointer.values()) {
            System.out.println("Select on or after pointer from each...");
            // Get subset of <Year, TreeMap> entry with year indexes after or including the pointer year
            NavigableMap<Integer, ArrayList<Event>> eventsOnOrAfterPointer = endYearMaps.tailMap(pointerYear, true);
            System.out.println(eventsOnOrAfterPointer);
            // Loop through each of ArrayList<Event> values
            System.out.println("Get events");
            for (ArrayList<Event> eventList: eventsOnOrAfterPointer.values()) {
                System.out.println(eventList);
                allPointerEvents.addAll(eventList);
            }
        }
        System.out.println("...Final list of events");
        System.out.println(allPointerEvents);
    }

    public static void main(String[] args) {
        // Test harness for eventIndex
        EventIndex eventIndex = new EventIndex();

        Event georgeWBushPresidency = new Event(
                new GregorianCalendar(2001, 0, 20),
                new GregorianCalendar(2009, 0, 20),
                new Annotation("George W Bush Presidency", "43rd US President")
        );

        Event barackObamaPresidency = new Event(
                new GregorianCalendar(2009, 0, 20),
                new GregorianCalendar(2017, 0, 20),
                new Annotation("Barack Obama Presidency", "44th US President")
        );
        eventIndex.addEvent(georgeWBushPresidency);
        eventIndex.addEvent(barackObamaPresidency);
        System.out.println(eventIndex.getPointerEvents(2010).get(0).toString());
        eventIndex.treeWalk(2008);
    }
}
