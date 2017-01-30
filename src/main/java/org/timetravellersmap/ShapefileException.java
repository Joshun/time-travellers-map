package org.timetravellersmap;

/**
 * ShapefileException: exception for when shapefile could not be read correctly
 */
public class ShapefileException extends TimeTravellersMapException {
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
