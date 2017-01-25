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
}
