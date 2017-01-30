package org.timetravellersmap.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.timetravellersmap.timeline.Event;

/**
 * SqliteQuery: carries out queries using SQLite
 */
public class SqliteQuery extends Query {
    private Connection connection;
    private String filepath;

    public SqliteQuery(String filepath) {
        this.filepath = filepath;
    }

    public void connect() throws QueryException{
        connection = null;
        try {
            String url = "jdbc:sqlite:" + filepath;
            connection = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite successful.");
        }
        catch (SQLException e) {
            throw new QueryException("Failed to connect to SQLite");
        }
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

    public static void main(String[] args) {
        SqliteQuery sq = new SqliteQuery("ttm.db");
        try {
            sq.connect();
        }
        catch (QueryException e) {
            System.out.println("Could not connect to db");
        }
    }
}
