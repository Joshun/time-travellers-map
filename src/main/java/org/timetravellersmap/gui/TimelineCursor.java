package org.timetravellersmap.gui;

/**
 * Created by joshua on 26/01/17.
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
