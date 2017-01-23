package org.timetravellersmap;

/**
 * Created by joshua on 23/01/17.
 */
public class ShapefileException extends TimetravellersmapException {
    public ShapefileException() {
        super();
    }
    public ShapefileException(String message) {
        super(message);
    }
    public ShapefileException(String message, Throwable cause) {
        super(message, cause);
    }
    public ShapefileException(Throwable cause) {
        super(cause);
    }
}
