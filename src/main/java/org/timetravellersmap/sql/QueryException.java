package org.timetravellersmap.sql;

import org.timetravellersmap.TimeTravellersMapException;

/**
 * QueryException: exception for query related errors
 */
public class QueryException extends TimeTravellersMapException {
    public QueryException() {
        super();
    }
    public QueryException(String message) {
        super(message);
    }
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }
    public QueryException(Throwable cause) {
        super(cause);
    }
}
