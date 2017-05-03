package org.timetravellersmap.basemapio;

import org.timetravellersmap.TimeTravellersMapException;

/**
 * BasemapIOException: exception used for loading basemaps
 */
public class BasemapIOException extends TimeTravellersMapException {
    public BasemapIOException() {
        super();
    }
    public BasemapIOException(String message) {
        super(message);
    }
    public BasemapIOException(String message, Throwable cause) {
        super(message, cause);
    }
    public BasemapIOException(Throwable cause) {
        super(cause);
    }
}
