/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.core.event;

import com.google.gson.annotations.Expose;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.LayerList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Event: a class to represent an event with start, end and annotation
 */
public class Event {
//    private LayerList layerList;
    @Expose
    private Descriptor eventDescriptor;

    @Expose
    private GregorianCalendar startDate;
    @Expose
    private GregorianCalendar endDate;
//    @Expose
//    private Layer layer = LayerList.DEFAULT_LAYER;
    @Expose
    private String layerName;
    @Expose
    private ArrayList<LayerComponent> layerComponents;

    public Event() {
        this(null, null, null);
    }

    public Event(GregorianCalendar startDate, GregorianCalendar endDate, Descriptor eventDescriptor) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventDescriptor = eventDescriptor;
        this.layerName = LayerList.DEFAULT_LAYER.getName();
        layerComponents = new ArrayList<>();
    }

    public Event(GregorianCalendar startDate, GregorianCalendar endDate, Descriptor eventDescriptor, ArrayList<LayerComponent> layerComponents) {
        this(startDate, endDate, eventDescriptor);
        this.layerComponents = layerComponents;
    }

    public Event(GregorianCalendar startDate, GregorianCalendar endDate, Descriptor eventDescriptor, String layerName) {
        this(startDate, endDate, eventDescriptor);
        this.layerName = layerName;
    }

    public void addLayerComponent(LayerComponent layerComponent) {
        layerComponents.add(layerComponent);
    }

    public void removeLayerComponent(LayerComponent layerComponent) {
        layerComponents.remove(layerComponent);
    }

    public Descriptor getEventDescriptor() {
        return eventDescriptor;
    }

    public int getStartDateAsYear() {
        return retrieveCalendarYear(startDate);
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public int getEndDateAsYear() {
        return retrieveCalendarYear(endDate);
    }

    private int retrieveCalendarYear(Calendar calendar) {
        // This ensures BC dates are handled properly
        int year = calendar.get(Calendar.YEAR);
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

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    public void setEventDescriptor(Descriptor descriptor) {
        this.eventDescriptor = descriptor;
    }

//    public Layer getLayer() {
//        return layer;
//    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

//    public void setLayer(Layer layer) {
//        this.layer = layer;
//    }

    public ArrayList<LayerComponent> getLayerComponents() {
        return layerComponents;
    }

    public void setLayerComponents(ArrayList<LayerComponent> layerComponents) {
        this.layerComponents = layerComponents;
    }

    public String toString() {
        return "event start="+getStartDateAsYear()+" end="+getEndDateAsYear()+" annotation="+ eventDescriptor;
    }

    public String getTooltipText() {
        return "<html>"
                + "<b>"
                + eventDescriptor.getName()
                + " (" + getStartDateAsYear() + " - " + getEndDateAsYear() + ")"
                + "</b><br>"
                + Descriptor.encodeMultilineHTML(eventDescriptor.getDescription())
                + "</html>";
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

}
