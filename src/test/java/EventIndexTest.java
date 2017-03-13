import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.core.event.EventIndex;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by joshua on 13/03/17.
 */
public class EventIndexTest extends TestCase {
    private EventIndex eventIndex;
    @Before
    public void setUp() {
        eventIndex = new EventIndex();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEmpty() {
        ArrayList<Event> events = eventIndex.getPointerEvents(1950);
        assertEquals(events.size(), 0);
    }

    @Test
    public void testAddEvent() {
        Event newEvent = new Event(
                new GregorianCalendar(1939, 8, 1),
                new GregorianCalendar(1945, 8, 2),
                new Descriptor("WWII", "Second World War")
        );
        eventIndex.addEvent(newEvent);
        // Testing leftmost bound case
        ArrayList<Event> eventsLeftBound = eventIndex.getPointerEvents(1939);
        assertEquals(eventsLeftBound.size(), 1);

        // Testing rightmost bound case
        ArrayList<Event> eventsRightBound = eventIndex.getPointerEvents(1945);
        assertEquals(eventsRightBound.size(), 1);

        // Testing centre case
        ArrayList<Event> eventsCentre = eventIndex.getPointerEvents(1942);
        assertEquals(eventsCentre.size(), 1);
    }

    @Test
    public void testRemoveEvent() {
        Event newEvent = new Event(
                new GregorianCalendar(1939, 8, 1),
                new GregorianCalendar(1945, 8, 2),
                new Descriptor("WWII", "Second World War")
        );
        eventIndex.addEvent(newEvent);

        ArrayList<Event> addedList = eventIndex.getStartEventsForYear(1939);
        assertEquals(addedList.size(), 1);

        eventIndex.removeEvent(newEvent);
        ArrayList<Event> removedList = eventIndex.getStartEventsForYear(1939);
        assertEquals(removedList.size(), 0);
    }

}
