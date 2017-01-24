package org.timetravellersmap;

/**
 * Created by joshua on 23/01/17.
 */
public class TimeTravellersMapException extends Exception {
    public TimeTravellersMapException() {
        super();
    }
    public TimeTravellersMapException(String message) {
        super(message);
    }
    public TimeTravellersMapException(String message, Throwable cause) {
        super(message, cause);
    }
    public TimeTravellersMapException(Throwable cause) {
        super(cause);
    }
}
