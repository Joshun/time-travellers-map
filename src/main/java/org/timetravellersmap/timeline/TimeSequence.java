package org.timetravellersmap.timeline;

import org.timetravellersmap.Annotation;

import java.util.Calendar;
import java.util.ArrayList;

/**
 * Created by joshua on 23/01/17.
 */
public class TimeSequence {

    private final Calendar startDate;
    private final Calendar endDate;
    private ArrayList<TimeFrame> timeFrames;
    private Annotation annotation;

    public TimeSequence(Calendar startDate, Calendar endDate, Annotation annotation) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.annotation = annotation;
    }

    public TimeSequence(Calendar startDate, Calendar endDate) {
        this(startDate, endDate, null);
    }
}
