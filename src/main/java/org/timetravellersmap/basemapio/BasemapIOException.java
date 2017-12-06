/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

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
