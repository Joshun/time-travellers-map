package org.timetravellersmap.sql;

import org.timetravellersmap.TimeTravellersMapException;

/**
 * Created by joshua on 01/03/17.
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
