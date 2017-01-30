package org.timetravellersmap.gui;

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
