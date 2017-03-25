package org.timetravellersmap.overlay;

import org.timetravellersmap.TimeTravellersMapException;

/**
 * Created by joshua on 25/03/17.
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
