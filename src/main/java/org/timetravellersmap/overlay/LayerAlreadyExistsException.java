package org.timetravellersmap.overlay;

import org.timetravellersmap.TimeTravellersMapException;

/**
 * Created by joshua on 25/03/17.
 */
public class LayerAlreadyExistsException extends TimeTravellersMapException {
    public LayerAlreadyExistsException() {
        super();
    }
    public LayerAlreadyExistsException(String message) {
        super(message);
    }
    public LayerAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public LayerAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
