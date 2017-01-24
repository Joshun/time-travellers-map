package org.timetravellersmap.timeline;

import org.timetravellersmap.Annotation;

import java.util.Calendar;

/**
 * Created by joshua on 23/01/17.
 */
public class TimeFrame {
    private final Calendar frameDate;
    private final Annotation annotation;

    public TimeFrame(Calendar frameDate, Annotation annotation) {
        this.frameDate = frameDate;
        this.annotation = annotation;
    }

    public TimeFrame(Calendar frameDate) {
        this(frameDate, null);
    }
}
