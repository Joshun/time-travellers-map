package org.timetravellersmap.sql;

import org.timetravellersmap.timeline.Event;

/**
 * Created by joshua on 24/01/17.
 */
public abstract class Query {
    public abstract void writeEvent(Event event);
    public abstract Event readEvent(int eventId);
    public abstract Event[] getAllEvents();
}
