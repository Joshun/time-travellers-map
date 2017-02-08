package org.timetravellersmap.core.event;

import org.timetravellersmap.core.Annotation;

import java.util.*;

/**
 * EventIndex: data structure for storing events indexed by their start and end years
 */
public class EventIndex {
    private TreeMap<Integer,TreeMap<Integer, ArrayList<Event>>> startYearIndex = new TreeMap<>();

    private TreeMap<Integer, Integer> startYearCounts = new TreeMap<>();
    private TreeMap<Integer, Integer> endYearCounts = new TreeMap<>();

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
        increaseYearCounts(startYear, endYear);
    }

    private void increaseYearCounts(int startYear, int endYear) {
        updateCount(startYearCounts, startYear, 1);
        updateCount(endYearCounts, endYear, 1);
    }

    private void reduceYearCounts(int startYear, int endYear) {
        updateCount(startYearCounts, startYear, -1);
        updateCount(endYearCounts, endYear, -1);
    }

    private void updateCount(TreeMap<Integer,Integer> countSet, int year, int increment) {
        Integer yearCountValue = countSet.get(year);
        if (yearCountValue == null) {
            yearCountValue = increment;
            countSet.put(year, yearCountValue);
        }
        else {
            System.out.println("increment=" + increment + "year=" + year);
            yearCountValue += increment;
            if (yearCountValue <= 0) {
                countSet.remove(year);
            }
            else {
                countSet.replace(year, yearCountValue);
            }
        }
    }



    public void updateEvent(Event oldEvent, Event newEvent) {
//        int startYear = oldEvent.getStartDateAsYear();
//        int endYear = oldEvent.getEndDateAsYear();
//        TreeMap<Integer, ArrayList<Event>> endYearMap = startYearIndex.get(startYear);
////        ArrayList<Event> eventList = endYearMap.get(endYear);
//        eventList.remove(oldEvent);

        // Here we are using removeEvent since this carrys out all the relevant cleanup operations
        removeEvent(oldEvent);
        reduceYearCounts(oldEvent.getStartDateAsYear(), oldEvent.getEndDateAsYear());

        // Here we are using addEvent since this reinserts it into the tree (the year(s) could have changed)
        addEvent(newEvent);
        increaseYearCounts(newEvent.getStartDateAsYear(), newEvent.getEndDateAsYear());
    }

    public void removeEvent(Event event) {
        int startYear = event.getStartDateAsYear();
        int endYear = event.getEndDateAsYear();
        TreeMap<Integer, ArrayList<Event>> endYearMap = startYearIndex.get(startYear);
        ArrayList<Event> eventList = endYearMap.get(endYear);

        // Remove event from list of events
        eventList.remove(event);

        // If the list of events is now empty, clean up the endYear, events entry
        if (eventList.size() == 0) {
            endYearMap.remove(endYear);
        }

        // If the endYear map is empty, clean up the startYear index entry
        if (endYearMap.size() == 0) {
            startYearIndex.remove(startYear);
        }
        reduceYearCounts(startYear, endYear);
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

    public int countStartEventsForYear(int year) {
        Integer count = startYearCounts.get(year);
        return (count != null) ? count : 0;
    }

    public int countEndEventsForYear(int year) {
        Integer count = endYearCounts.get(year);
        return (count != null) ? count : 0;
    }

    public ArrayList<Event> getStartEventsForYear(int year) {
        ArrayList<Event> events = new ArrayList<>();
        TreeMap<Integer, ArrayList<Event>> endYearEventIndex = startYearIndex.get(year);
        if (endYearEventIndex != null) {
            for (ArrayList<Event> el: endYearEventIndex.values()) {
                events.addAll(el);
            }
            return events;
        }
        else {
            return null;
        }
    }

    public String generateStartEventSummary(int year) {
        ArrayList<Event> events = getStartEventsForYear(year);
        if (events == null) {
            return null;
        }
        String eventString = "<html>";
        eventString += "<b>" + year + "</b><br>";

        int count = 0;
        for (Event event: events) {
            eventString += event.getEventAnnotation().getName() + "<br>";
            count++;
            if (count > 9) {
                eventString += "...";
                break;
            }
        }
        eventString += "</html>";
        return eventString;
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
//        eventIndex.treeWalk(2008);
        eventIndex.removeEvent(georgeWBushPresidency);
        eventIndex.treeWalk(2008);

        eventIndex.updateEvent(barackObamaPresidency, new Event(
                new GregorianCalendar(1861, 3, 4),
                new GregorianCalendar(1865, 4, 15),
                new Annotation("Abraham Lincoln Presidency", "16th US President")
        ));
        eventIndex.treeWalk(2010);
        eventIndex.treeWalk(1864);
    }
}
