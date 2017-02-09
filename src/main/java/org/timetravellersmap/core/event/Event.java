package org.timetravellersmap.core.event;

import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Event: a class to represent an event with start, end and annotation
 */
public class Event {
//    private LayerList layerList;
    private Descriptor eventDescriptor;

    private Calendar startDate;
    private Calendar endDate;

    private Event parentEvent = null;
    private ArrayList<Event> childEvents = null;

    private Layer layer = LayerList.DEFAULT_LAYER;

    public Event(Calendar startDate, Calendar endDate, Descriptor eventDescriptor) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventDescriptor = eventDescriptor;
//        this.layerList = new LayerList();
    }

    public Event(Calendar startDate, Calendar endDate, Descriptor eventDescriptor, Layer layer) {
        this(startDate, endDate, eventDescriptor);
        this.layer = layer;
    }

    public Descriptor getEventDescriptor() {
        return eventDescriptor;
    }

    public int getStartDateAsYear() {
        return retrieveCalendarYear(startDate, Calendar.YEAR);
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public int getEndDateAsYear() {
        return retrieveCalendarYear(endDate, Calendar.YEAR);
    }

    private int retrieveCalendarYear(Calendar calendar, int calendarField) {
        // This ensures BC dates are handled properly
        int year = calendar.get(calendarField);
        // If ERA field is set to BC, year is expected to be negative by GUI
        if (calendar.get(Calendar.ERA) == GregorianCalendar.BC) {
            return -year;
        }
        // If AD, leave positive
        else {
            return year;
        }
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public void setEventDescriptor(Descriptor descriptor) {
        this.eventDescriptor = descriptor;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public String toString() {
        return "event start="+getStartDateAsYear()+" end="+getEndDateAsYear()+" annotation="+ eventDescriptor;
    }

    public static void main(String[] args) {
        // Test harness for Event
        Event event = new Event(
                new GregorianCalendar(1990, 1, 1),
                new GregorianCalendar(1910, 1, 1),
                new Descriptor("Test event", "this is a description")
        );
        System.out.println("Event toString: " + event.toString());
        System.out.println("getStartDate " + event.getStartDate());
        System.out.println("getStartDateAsYear " + event.getStartDateAsYear());
        System.out.println("getEndDate " + event.getEndDate());
        System.out.println("getStartDateAsYear " + event.getEndDateAsYear());

        ArrayList<Event> elist = new ArrayList<>();
        elist.add(event);
        System.out.println(elist.get(0));
    }

//    public LayerList getLayerList() {
//        return layerList;
//    }
}
