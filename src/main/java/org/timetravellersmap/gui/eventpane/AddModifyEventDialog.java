/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.core.event.EventChangeListener;
import org.timetravellersmap.overlay.Layer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

/**
 * AddModifyEventDialog: dialog for adding new events or modifying existing events
 * Used by EventPane which displays the list of current events for the time period
 */
public class AddModifyEventDialog extends JFrame {
    private final static Logger LOGGER = Logger.getLogger(AddModifyEventDialog.class.getName());
    private JPanel panel;
    private Event event;
    private JTextField eventNameField;
    private JTextArea eventDescriptionField;
    private JSpinner startDateField;
    private JSpinner endDateField;
    private JButton cancelButton;
    private JButton confirmButton;
    private JComboBox<Layer> eventLayerComboBox;

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

        int startYearInitialValue = pointerYear;

        // Begin layout of GUI components
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
        gc.gridy = 4;
        JLabel eventLayerLabel = new JLabel("Layer:");
        panel.add(eventLayerLabel, gc);

        gc.gridx = 1;
        gc.gridy = 4;
        eventLayerComboBox = new JComboBox<>();
        eventLayerComboBox.setModel(new DefaultComboBoxModel<>(mapFrame.getLayerList().getLayers()));
        panel.add(eventLayerComboBox, gc);

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

        this.add(panel);
        this.pack();
        // End layout of GUI components

        // Begin adding listeners
        confirmButton.addActionListener(actionEvent ->  {
            if (existingEvent != null) {
                updateOrAddEvent(existingEvent);
            }
            else {
                updateOrAddEvent(null);
            }
        });

        JFrame parentFrame = this;
        cancelButton.addActionListener(actionEvent ->  {
                    parentFrame.dispose();
                }
        );
        // End adding listeners

        if (existingEvent != null) {
            loadExistingEvent(existingEvent);
        }

        this.setVisible(true);
    }

    public AddModifyEventDialog(MapFrame parentMapFrame, EventPane eventPane, int timelinePointerYear) {
        this(null, parentMapFrame, eventPane, timelinePointerYear);
    }

    private void loadExistingEvent(Event existingEvent) {
        if (existingEvent == null) {
            return;
        }

        eventNameField.setText(existingEvent.getEventDescriptor().getName());
        eventDescriptionField.setText(existingEvent.getEventDescriptor().getDescription());
        startDateField.setValue(existingEvent.getStartDateAsYear());
        endDateField.setValue(existingEvent.getEndDateAsYear());
    }

    public static GregorianCalendar yearToCalendar(int year) {
        int eraField;
        if (year < 0) {
            eraField = GregorianCalendar.BC;
            year = -year;
        }
        else {
            eraField = GregorianCalendar.AD;
        }
        GregorianCalendar calendar = new GregorianCalendar(year, 0, 1);
        calendar.set(Calendar.ERA, eraField);
        return calendar;
    }

    public static int calendarToYear(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int era = calendar.get(Calendar.ERA);
        if (era == GregorianCalendar.BC) {
            year = -year;
        }
        return year;
    }

    public static int dateToYear(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return calendarToYear(cal);
    }

    private void updateOrAddEvent(Event existingEvent) {
        // if existingEvent is null, add new event
        // else update existing event
        LOGGER.info("Updating event " + existingEvent);
        String eventName = eventNameField.getText();
        String eventDescription = eventDescriptionField.getText();
        int startDate = (int)startDateField.getValue();
        int endDate = (int)endDateField.getValue();
        Layer layer = (Layer)eventLayerComboBox.getSelectedItem();

        Event newEvent = new Event(
                yearToCalendar(startDate),
                yearToCalendar(endDate),
                new Descriptor(eventName, eventDescription),
                layer.getName()
        );

        if (eventName.length() == 0) {
            showInputError("Event must have a name!");
        }
        else if (endDateBeforeStartDate(startDate, endDate)) {
            showDateError("End date cannot occur before start date!");
        }
        else if (datesAreZero(startDate, endDate)) {
            showDateError("Year zero does not exist!");
        }
        else {
            if (existingEvent != null) {
                newEvent.setLayerComponents(existingEvent.getLayerComponents());
                eventPane.updateExistingEvent(existingEvent, newEvent);
                mapFrame.getEventIndex().updateEvent(existingEvent, newEvent);
            } else {
                eventPane.addNewEvent(newEvent);
                mapFrame.getEventIndex().addEvent(newEvent);
            }
            fireChangeListeners();
            this.dispose();
        }
    }

    private static boolean endDateBeforeStartDate(int startDate, int endDate) {
        return endDate <= startDate;
    }

    private static boolean datesAreZero(int startDate, int endDate) {
        return startDate == 0 || endDate == 0;
    }

    private void showDateError(String dateErrorMessage) {
        JOptionPane.showMessageDialog(this, dateErrorMessage,"Date error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInputError(String inputErrorMessage) {
        JOptionPane.showMessageDialog(this, inputErrorMessage, "Input error", JOptionPane.ERROR_MESSAGE);
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
//                new Descriptor("Battle of Hastings", "William Duke of Normandy vs Harold Godwinson")
//            );
//        new AddModifyEventDialog(hastings, new EventPane(), new EventIndex(), null);
    }
}
