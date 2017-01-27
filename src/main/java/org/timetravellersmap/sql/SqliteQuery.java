package org.timetravellersmap.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.timetravellersmap.timeline.Event;

/**
 * Created by joshua on 24/01/17.
 */
public class SqliteQuery extends Query {
    public SqliteQuery() {

    }

    @Override
    public Event readEvent(int eventId) {
        return null;
    }

    @Override
    public void writeEvent(Event event) {

    }

    @Override
    public Event[] getAllEvents() {
        return new Event[0];
    }
}
