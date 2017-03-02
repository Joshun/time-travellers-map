package org.timetravellersmap.core.event;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.overlay.Layer;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.LayerList;
import org.timetravellersmap.sql.DBLayerComponent;
import org.timetravellersmap.sql.QueryException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

/**
 * Event: a class to represent an event with start, end and annotation
 */
@DatabaseTable(tableName = "events")
public class Event {
//    private LayerList layerList;
    private Logger LOGGER = Logger.getLogger(Event.class.getName());
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Descriptor eventDescriptor;

    @DatabaseField
    private Date startDate;
    @DatabaseField
    private Date endDate;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Layer layer = LayerList.DEFAULT_LAYER;
    @ForeignCollectionField
    private ForeignCollection<DBLayerComponent> layerComponents;
//    private ArrayList<LayerComponent> layerComponents = new ArrayList<>();

    @DatabaseField(generatedId = true)
    private int id;


    public Event() {
        this(null, null, null);
    }

    public Event(Calendar startDate, Calendar endDate, Descriptor eventDescriptor) {
//        try {
//            layerComponents.refreshCollection();
//        }
//        catch (SQLException e) {
//            LOGGER.warning("Failed to refresh layerComponents for event " + this);
//        }
        this.startDate = startDate.getTime();
        this.endDate = endDate.getTime();
        this.eventDescriptor = eventDescriptor;
    }

    public Event(Calendar startDate, Calendar endDate, Descriptor eventDescriptor, Layer layer) {
        this(startDate, endDate, eventDescriptor);
        this.layer = layer;
    }

    public void addLayerComponent(LayerComponent layerComponent) {
//        layerComponents.add(layerComponent);
        layerComponents.add(new DBLayerComponent(layerComponent));
    }

    public void removeLayerComponent(LayerComponent layerComponent) {
        layerComponents.remove(layerComponent);
    }

    public Descriptor getEventDescriptor() {
        return eventDescriptor;
    }

    public int getStartDateAsYear() {
        return retrieveCalendarYear(dateToCalendar(startDate), Calendar.YEAR);
    }

    public Calendar getStartDate() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(startDate);
        return cal;
    }

    public Calendar getEndDate() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(endDate);
        return cal;
    }

    public Date getStartDateAsDate() {
        return startDate;
    }

    public int getEndDateAsYear() {
        return retrieveCalendarYear(dateToCalendar(endDate), Calendar.YEAR);
    }

    private static Calendar dateToCalendar(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }

    private int retrieveCalendarYear(Calendar calendar, int calendarField) {
        // This ensures BC dates are handled properly
        int year = calendar.get(calendarField);
        // If ERA field is set to BC, year is expected to be negative by GUI
        if (calendar.get(Calendar.ERA) == GregorianCalendar.BC) {
            return -year;
        }
        // If AD, leave positive
        else {
            return year;
        }
    }

    public Date getEndDateAsDate() {
        return endDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate.getTime();
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate.getTime();
    }

    public void setEventDescriptor(Descriptor descriptor) {
        this.eventDescriptor = descriptor;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

//    public ForeignCollection<LayerComponent> getLayerComponents() {
//        return layerComponents;
//    }

    public ArrayList<LayerComponent> getLayerComponentsArrayList() {
        return new ArrayList<>(layerComponents);
    }

    public String toString() {
        return "event start="+getStartDateAsYear()+" end="+getEndDateAsYear()+" annotation="+ eventDescriptor;
    }

    public static void main(String[] args) {
        // Test harness for Event
        Event event = new Event(
                new GregorianCalendar(1990, 1, 1),
                new GregorianCalendar(1910, 1, 1),
                new Descriptor("Test event", "this is a description")
        );
        System.out.println("Event toString: " + event.toString());
        System.out.println("getStartDate " + event.getStartDate());
        System.out.println("getStartDateAsYear " + event.getStartDateAsYear());
        System.out.println("getEndDate " + event.getEndDate());
        System.out.println("getStartDateAsYear " + event.getEndDateAsYear());

        ArrayList<Event> elist = new ArrayList<>();
        elist.add(event);
        System.out.println(elist.get(0));
    }

}
