package org.timetravellersmap;

/**
 * TimeTravellersMapException: generic exception for the program
 * All other exceptions in the program should inherit from this
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
