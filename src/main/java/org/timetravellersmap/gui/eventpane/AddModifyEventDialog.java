package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.core.Annotation;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.core.event.EventChangeListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * AddModifyEventDialog: dialog for adding new events or modifying existing events
 * Used by EventPane which displays the list of current events for the time period
 */
public class AddModifyEventDialog extends JFrame {
//    private JFrame parentFrame;
    private JPanel panel;
    private Event event;
    private JTextField eventNameField;
    private JTextArea eventDescriptionField;
    private JSpinner startDateField;
    private JSpinner endDateField;
    private JButton cancelButton;
    private JButton confirmButton;

    private int textBoxWidth = 25;
    private int descriptionRows = 3;

    private MapFrame mapFrame;
    private EventPane eventPane;

    private ArrayList<EventChangeListener> changeListeners = new ArrayList<>();

    public AddModifyEventDialog(Event existingEvent, MapFrame ancestorMapFrame, EventPane parentEventPane, int timelinePointerYear) {
        this.mapFrame = ancestorMapFrame;
        this.event = existingEvent;
        this.eventPane = parentEventPane;

        this.changeListeners = parentEventPane.getChangeListeners();

//        Integer pointerYear = ancestorMapFrame.getTimelineWidget().getPointerYear();
        int pointerYear = timelinePointerYear;

        if (event == null) {
            setTitle("Create new event");
        }
        else {
            setTitle("Edit event");
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int startYearInitialValue = pointerYear;
//        if (pointerYear == null) {
//            startYearInitialValue = 1900;
//        }
//        else {
//        startYearInitialValue = pointerYear;
//        }

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0.5;
        gc.weighty = 0.5;
        gc.ipadx = 5;

        gc.gridx = 0;
        gc.gridy = 0;
        JLabel eventNameFieldLabel = new JLabel("Name:");
        panel.add(eventNameFieldLabel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        eventNameField = new JTextField(textBoxWidth);
        panel.add(eventNameField, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        JLabel eventDescriptionFieldLabel = new JLabel("Description:");
        panel.add(eventDescriptionFieldLabel, gc);

        gc.gridx = 1;
        gc.gridy = 1;
        eventDescriptionField = new JTextArea("", descriptionRows, textBoxWidth);
        panel.add(eventDescriptionField, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        JLabel startDateFieldLabel = new JLabel("Start date:");
        panel.add(startDateFieldLabel, gc);


        gc.gridx = 1;
        gc.gridy = 2;
        startDateField = new JSpinner(new SpinnerNumberModel(startYearInitialValue, -4000, 4000, 1));
        startDateField.setEditor(new JSpinner.NumberEditor(startDateField, "#"));

        panel.add(startDateField, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        JLabel endDateFieldLabel = new JLabel("End date:");
        panel.add(endDateFieldLabel, gc);

        gc.gridx = 1;
        gc.gridy = 3;
        endDateField = new JSpinner(new SpinnerNumberModel(startYearInitialValue+1, -4000, 4000, 1));
        endDateField.setEditor(new JSpinner.NumberEditor(endDateField, "#"));
        panel.add(endDateField, gc);

        gc.gridx = 0;
        gc.gridy = 5;
        if (existingEvent == null) {
            confirmButton = new JButton("Add event");
        }
        else {
            confirmButton = new JButton("Update event");
        }
        panel.add(confirmButton, gc);

        gc.gridx = 1;
        gc.gridy = 5;
        cancelButton = new JButton("Cancel");
        panel.add(cancelButton, gc);

        if (existingEvent != null) {
            loadExistingEvent(existingEvent);
        }

        confirmButton.addActionListener(actionEvent ->  {
            if (existingEvent != null) {
                updateOrAddEvent(existingEvent);
            }
            else {
//                    System.out.println("TODO: adding events");
                updateOrAddEvent(null);
            }
        });

        JFrame parentFrame = this;
        cancelButton.addActionListener(actionEvent ->  {
                parentFrame.dispose();
            }
        );

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    public AddModifyEventDialog(MapFrame parentMapFrame, EventPane eventPane, int timelinePointerYear) {
        this(null, parentMapFrame, eventPane, timelinePointerYear);
    }

    private void loadExistingEvent(Event existingEvent) {
        if (existingEvent == null) {
            return;
        }

        eventNameField.setText(existingEvent.getEventAnnotation().getName());
        eventDescriptionField.setText(existingEvent.getEventAnnotation().getDescription());
        startDateField.setValue(existingEvent.getStartDateAsYear());
        endDateField.setValue(existingEvent.getEndDateAsYear());
    }

    private Calendar yearToCalendar(int year) {
        int eraField;
        if (year < 0) {
            eraField = GregorianCalendar.BC;
            year = -year;
        }
        else {
            eraField = GregorianCalendar.AD;
        }
        Calendar calendar = new GregorianCalendar(year, 0, 1);
        calendar.set(Calendar.ERA, eraField);
        return calendar;
    }

    private void updateOrAddEvent(Event existingEvent) {
        // if existingEvent is null, add new event
        // else update existing event
        System.out.println("Updating event " + existingEvent);
        String eventName = eventNameField.getText();
        String eventDescription = eventDescriptionField.getText();
        int startDate = (int)startDateField.getValue();
        int endDate = (int)endDateField.getValue();

        Event newEvent = new Event(
                yearToCalendar(startDate),
                yearToCalendar(endDate),
                new Annotation(eventName, eventDescription)
        );
        if (datesAreOk(startDate, endDate)) {
            if (existingEvent != null) {
                eventPane.updateExistingEvent(existingEvent, newEvent);
                mapFrame.getEventIndex().updateEvent(existingEvent, newEvent);
            } else {
                eventPane.addNewEvent(newEvent);
                mapFrame.getEventIndex().addEvent(newEvent);
            }
//            mapFrame.getTimelineWidget().redraw();
            fireChangeListeners();
            this.dispose();
        }
        else {
            showDateError();
        }
    }

    private boolean datesAreOk(int startDate, int endDate) {
        return endDate >= startDate;
    }

    private void showDateError() {
        JOptionPane.showMessageDialog(this, "End date cannot occur before start date!","Date error", JOptionPane.ERROR_MESSAGE);
    }

    public void addChangeListener(EventChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    public void removeChangeListener(EventChangeListener changeListener) {
        changeListeners.remove(changeListener);
    }

    public void fireChangeListeners() {
        for (EventChangeListener changeListener: changeListeners) {
            changeListener.eventChanged();
        }
    }

    public static void main(String[] args) {
        // test harness for AddModifyEventDialog
//        JFrame toplevel = new JFrame();
//        toplevel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        toplevel.add(new AddModifyEventDialog());
//        toplevel.pack();
//        toplevel.setVisible(true);
//        new AddModifyEventDialog(new EventPane(), new EventIndex());
//        Event hastings = new Event(
//                new GregorianCalendar(1066, 6, 14),
//                new GregorianCalendar(1066, 6, 15),
//                new Annotation("Battle of Hastings", "William Duke of Normandy vs Harold Godwinson")
//            );
//        new AddModifyEventDialog(hastings, new EventPane(), new EventIndex(), null);
    }
}
