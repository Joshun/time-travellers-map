package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.Annotation;
import org.timetravellersmap.timeline.Event;
import org.timetravellersmap.timeline.EventIndex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
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

    private EventPane eventPane;
    private EventIndex eventIndex;

    public AddModifyEventDialog(Event existingEvent, EventPane eventPane, EventIndex eventIndex) {
        this.event = existingEvent;
        this.eventPane = eventPane;
        this.eventIndex = eventIndex;

        if (event == null) {
            setTitle("Create new event");
        }
        else {
            setTitle("Edit event");
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        if (event == null) {
//            this.parentFrame = new JFrame("Create new event");
//        }
//        else {
//            this.parentFrame = new JFrame("Edit event");
//        }
//        this.parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        startDateField = new JSpinner(new SpinnerNumberModel(1900, -4000, 4000, 1));
        startDateField.setEditor(new JSpinner.NumberEditor(startDateField, "#"));
//        Date initialDate = new GregorianCalendar(-2, 0, 1).getTime();
//        Date earliestDate = new GregorianCalendar(-4000, 0, 1).getTime();
//        Date latestDate = new GregorianCalendar(4000, 0, 1).getTime();
//        startDateField = new JSpinner(new SpinnerDateModel(initialDate, earliestDate, latestDate, Calendar.YEAR));
//        startDateField.setEditor(new JSpinner.DateEditor(startDateField, "MM/yyyy"));
        panel.add(startDateField, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        JLabel endDateFieldLabel = new JLabel("End date:");
        panel.add(endDateFieldLabel, gc);

        gc.gridx = 1;
        gc.gridy = 3;
        endDateField = new JSpinner(new SpinnerNumberModel(1900, -4000, 4000, 1));
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

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (existingEvent != null) {
                    updateOrAddEvent(existingEvent);
                }
                else {
//                    System.out.println("TODO: adding events");
                    updateOrAddEvent(null);
                }
            }
        });

        JFrame parentFrame = this;
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                parentFrame.dispose();
            }
        });

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    public AddModifyEventDialog(EventPane eventPane, EventIndex eventIndex) {
        this(null, eventPane, eventIndex);
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

    private void updateOrAddEvent(Event existingEvent) {
        // if existingEvent is null, add new event
        // else update existing event
        System.out.println("Updating event " + existingEvent);
        String eventName = eventNameField.getText();
        String eventDescription = eventDescriptionField.getText();
        int startDate = (int)startDateField.getValue();
        int endDate = (int)endDateField.getValue();

        Event newEvent = new Event(
                new GregorianCalendar(startDate, 0, 1),
                new GregorianCalendar(endDate, 0, 1),
                new Annotation(eventName, eventDescription)
        );
        if (existingEvent != null) {
            eventPane.updateExistingEvent(existingEvent, newEvent);
            eventIndex.updateEvent(existingEvent, newEvent);
        }
        else {
            eventPane.addNewEvent(newEvent);
            eventIndex.addEvent(newEvent);
        }
        this.dispose();
    }

    public static void main(String[] args) {
        // test harness for AddModifyEventDialog
//        JFrame toplevel = new JFrame();
//        toplevel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        toplevel.add(new AddModifyEventDialog());
//        toplevel.pack();
//        toplevel.setVisible(true);
        new AddModifyEventDialog(new EventPane(), new EventIndex());
        Event hastings = new Event(
                new GregorianCalendar(1066, 6, 14),
                new GregorianCalendar(1066, 6, 15),
                new Annotation("Battle of Hastings", "William Duke of Normandy vs Harold Godwinson")
            );
        new AddModifyEventDialog(hastings, new EventPane(), new EventIndex());
    }
}
