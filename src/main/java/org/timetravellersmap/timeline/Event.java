package org.timetravellersmap.timeline;

import org.timetravellersmap.Annotation;
import org.timetravellersmap.overlay.LayerList;

import java.util.ArrayList;

/**
 * Created by joshua on 24/01/17.
 */
public class Event {
    private TimeSequence timeSequence;
    private LayerList layerList;
    private Annotation eventAnnotation;

    private Event parentEvent;
    private ArrayList<Event> childEvents;
}
