package org.timetravellersmap.timeline;

import org.timetravellersmap.Annotation;
import org.timetravellersmap.overlay.LayerList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Event: a class to represent an event with start, end and annotation
 */
public class Event {
    private LayerList layerList;
    private Annotation eventAnnotation;

    private Calendar startDate;
    private Calendar endDate;

    private Event parentEvent;
    private ArrayList<Event> childEvents;

    public Event(Calendar startDate, Calendar endDate, Annotation eventAnnotation) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventAnnotation = eventAnnotation;
    }

    public Annotation getEventAnnotation() {
        return eventAnnotation;
    }

    public int getStartDateAsYear() {
        return startDate.get(Calendar.YEAR);
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public int getEndDateAsYear() {
        return endDate.get(Calendar.YEAR);
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

    public void setEventAnnotation(Annotation annotation) {
        this.eventAnnotation = annotation;
    }

    public static void main(String[] args) {
        // Test harness for Event
        Event event = new Event(
                new GregorianCalendar(1990, 1, 1),
                new GregorianCalendar(1910, 1, 1),
                new Annotation("Test event", "this is a description")
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
}
