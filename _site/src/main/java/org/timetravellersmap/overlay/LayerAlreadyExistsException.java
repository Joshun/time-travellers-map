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
 * LayerAlreadyExistsException: exception thrown when a duplicate layer is created
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
