package org.timetravellersmap;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.timetravellersmap.core.timeline.Timeline;
import org.timetravellersmap.core.timeline.TimelineCursor;

/**
 * Created by joshua on 17/03/17.
 */
public class TimelineTest extends TestCase {
    private static final int START_YEAR = 1900;
    private static final int END_YEAR = 2000;
    private static final int MINOR_INTERVAL = 1;
    private static final int MAJOR_INTERVAL = 10;


    private int[] testMinorYears;
    private int[] testMajorYears;
    private Timeline timeline;

    @Before
    public void setUp() {
        System.out.println("Timeline tester");
        timeline = new Timeline(START_YEAR, END_YEAR, MINOR_INTERVAL, MAJOR_INTERVAL);

        int testMinorYearsSize = Math.round((END_YEAR-START_YEAR)/MINOR_INTERVAL) + 1;
        testMinorYears = new int[testMinorYearsSize];
        System.out.println("Generating testminyears (" + testMinorYearsSize + ")...");
        for (int i=0; i<testMinorYearsSize; i++) {
            int year = START_YEAR + (i * MINOR_INTERVAL);
            testMinorYears[i] = year;
            System.out.print(year);
            System.out.print("...");
        }
        System.out.println();

        int testMajorYearsSize = Math.round((END_YEAR-START_YEAR)/MAJOR_INTERVAL) + 1;
        testMajorYears = new int[testMajorYearsSize];
        System.out.println("Generating testmajyears (" + testMajorYearsSize + ")...");
        for (int i=0; i<testMajorYearsSize; i++) {
            int year = START_YEAR + (i * MAJOR_INTERVAL);
            testMajorYears[i] = year;
            System.out.print(year);
            System.out.print("...");
        }
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testIntervals() {
        assertEquals((int)timeline.getMinorInterval(), MINOR_INTERVAL);
        assertEquals((int)timeline.getMajorInterval(), MAJOR_INTERVAL);
    }

    @Test
    public void testIterator() {
        int minorPos = 0;
        int majorPos = 0;
        for (TimelineCursor timelineCursor: timeline) {
            if (timelineCursor.getPosition() != testMinorYears[minorPos]) {
                fail("Invalid minor interval " + timelineCursor.getPosition());
            }
            else {
                minorPos++;
            }
            if (timelineCursor.isMajorInterval()) {
                if (timelineCursor.getPosition() != testMajorYears[majorPos]) {
                    fail("Invalid major interval " + timelineCursor.getPosition());
                }
                else {
                    majorPos++;
                }
            }
        }
        assertEquals(minorPos, testMinorYears.length);
        assertEquals(majorPos, testMajorYears.length);
    }
}
