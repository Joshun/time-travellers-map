package org.timetravellersmap.overlay;

import org.timetravellersmap.TimeTravellersMapException;

/**
 * AttemptRemoveDefaultLayerException: exception thrown when an attempt is made to remove the
 *  "Default" layer, this must not be removed as it is the fallback layer for events that are
 *  orphaned from a layer
 */
public class AttemptRemoveDefaultLayerException extends TimeTravellersMapException {
    public AttemptRemoveDefaultLayerException() {
        super();
    }
    public AttemptRemoveDefaultLayerException(String message) {
        super(message);
    }
    public AttemptRemoveDefaultLayerException(String message, Throwable cause) {
        super(message, cause);
    }
    public AttemptRemoveDefaultLayerException(Throwable cause) {
        super(cause);
    }
}
