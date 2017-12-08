/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.core.timeline;

/**
 * TimelineCursor: the object returned by Timeline iterable
 * Gives current position and the type of interval (major/minor)
 */
public class TimelineCursor {
    private double position;
    private boolean isMajorIntervalFlag;

    public TimelineCursor(double position, boolean isMajorIntervalFlag) {
        this.position = position;
        this.isMajorIntervalFlag = isMajorIntervalFlag;
    }

    public double getPosition() {
        return position;
    }

    public boolean isMajorInterval() {
        return this.isMajorIntervalFlag;
    }
}
