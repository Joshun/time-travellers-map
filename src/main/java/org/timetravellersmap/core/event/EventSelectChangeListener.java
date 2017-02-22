package org.timetravellersmap.core.event;

/**
 * EventSelectChangeListener: used by TimelineWidget, AnnotatePane to know when the selected event changes
 */
public interface EventSelectChangeListener {
    void eventSelected(Event event);
    void eventDeselected();
}
