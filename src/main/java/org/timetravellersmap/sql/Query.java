package org.timetravellersmap.sql;

import org.timetravellersmap.timeline.Event;

/**
 * Query: class which all query engines must inherit from
 */
public abstract class Query {
    public abstract void writeEvent(Event event);
    public abstract Event readEvent(int eventId);
    public abstract Event[] getAllEvents();
}
