package org.timetravellersmap.gui;

import java.sql.Time;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Timeline: an iterable object for generating timeline rulers
 * The minor intervals are the small lines, e.g. 1 year, and the major intervals large lines,
 * e.g. 10 years
 * Used by TimelineWidget to draw the timeline ruler
 */
public class Timeline implements Iterable<TimelineCursor> {
    private double start;
    private double end;
    private double minorInterval;
    private double majorInterval;

    public Timeline() {
        this.start = 1900.0;
        this.end = 2000.0;
        this.majorInterval = 20;
        this.minorInterval = 10;
    }

    public Timeline(double start, double end) {
        this.start = start;
        this.end = end;
        double minorInterval = (end-start)/10.0;
        double majorInterval = minorInterval*4.0;
        this.minorInterval = minorInterval;
        this.majorInterval = majorInterval;
        System.out.println("min " + minorInterval + " maj " + majorInterval);
    }

    public Timeline(double start, double end, double minorInterval, double majorInterval) {
        this(start, end);
        this.minorInterval = minorInterval;
        this.majorInterval = majorInterval;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public double getMinorInterval() {
        return minorInterval;
    }

    public double getMajorInterval() {
        return majorInterval;
    }

    @Override
    public Iterator<TimelineCursor> iterator() {
        return new Iterator<TimelineCursor>() {
            private double currentValue = start;

            public boolean hasNext() {
                return this.currentValue < (end + minorInterval);
            }

            private boolean isMajorInterval() {
                return currentValue % majorInterval == 0;
            }

            public TimelineCursor next() {
                if (hasNext()) {
                    double prevValue = currentValue;
                    boolean prevIsMajor = isMajorInterval();
                    currentValue += minorInterval;
                    return new TimelineCursor(prevValue, prevIsMajor);
                }
                else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    public static void main(String[] args) {
        Timeline t = new Timeline(1900, 2000);
        for (TimelineCursor tc: t) {
            System.out.println(tc.getPosition());
            System.out.println(tc.isMajorInterval());
        }
    }
}
