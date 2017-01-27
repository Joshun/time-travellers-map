package org.timetravellersmap.timeline;

import org.timetravellersmap.Annotation;
import org.timetravellersmap.overlay.LayerList;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by joshua on 24/01/17.
 */
public class Event {
    private LayerList layerList;
    private Annotation eventAnnotation;

    private Calendar startDate;
    private Calendar endDate;

    private Event parentEvent;
    private ArrayList<Event> childEvents;

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
}
