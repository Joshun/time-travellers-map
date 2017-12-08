/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

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
