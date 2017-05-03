package org.timetravellersmap.core.timeline;

/**
 * TimelineChangeListener: interface for objects listening to changes in the timeline year selection
 */
public interface TimelineChangeListener {
    void timelineChanged(int year, boolean redraw);
}
