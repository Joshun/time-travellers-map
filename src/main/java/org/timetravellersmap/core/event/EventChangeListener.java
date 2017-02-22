package org.timetravellersmap.core.event;

/**
 * EventChangeListener: listen to add, removal or edit of events
 * Used by EventPane to notify AnnotatePane and TimelineWidget about changes
 */
public interface EventChangeListener {
    void eventChanged();
}
