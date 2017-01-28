package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.Annotation;
import org.timetravellersmap.timeline.Event;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by joshua on 27/01/17.
 */
public class EventPane extends JPanel {
    private JScrollPane eventTableContainer;
    private JTable eventTable;
    private JButton addEventButton;
    private JButton removeEventButton;
    private JButton editEventButton;

    private ArrayList<Event> currentEvents = new ArrayList<>();

    public EventPane() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        String[] eventTableColumns = new String[] { "Event", "Start", "End" };

        currentEvents.add(new Event(
                new GregorianCalendar(1900, 0, 1),
                new GregorianCalendar(1910, 0, 1),
                new Annotation("Test event", "This is a description")
        ));

        eventTable = new JTable(new TableModel() {
            @Override
            public int getRowCount() {
//                return currentEvents.size();
                return 1;
            }

            @Override
            public int getColumnCount() {
                return eventTableColumns.length;
            }

            @Override
            public String getColumnName(int i) {
                System.out.println("callback");
                if (i < eventTableColumns.length) {
                    return eventTableColumns[i];
                }
                else {
                    return null;
                }
            }

            @Override
            public Class<?> getColumnClass(int i) {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }

            @Override
            public Object getValueAt(int i, int i1) {
                if (i < currentEvents.size() && i1 < eventTableColumns.length) {
                    Event event = currentEvents.get(i);
                    if (i1 == 0) {
                        return event.getEventAnnotation().getName();
                    } else if (i1 == 1) {
                        return String.valueOf(event.getStartDateAsYear());
                    } else if (i1 == 2) {
                        return String.valueOf(event.getEndDateAsYear());
                    } else {
                        return null;
                    }
                }
                else {
                    return null;
                }
            }

            @Override
            public void setValueAt(Object o, int i, int i1) {
                if (i < currentEvents.size()) {
                    Event event = currentEvents.get(i);
                    Annotation oldAnnotation = event.getEventAnnotation();

                    if (i1 == 0) {
                        String name = oldAnnotation.getName();
                        String description = oldAnnotation.getDescription();
                        name = o.toString();
                        Annotation newAnnotation = new Annotation(name, description);
                        event.setEventAnnotation(newAnnotation);
                    }
                    else if (i1 == 1) {
                        Calendar calendar = new GregorianCalendar((int)o, 0, 1);
                        event.setStartDate(calendar);
                    }
                    else if (i1 == 2) {
                        Calendar calendar = new GregorianCalendar((int)o, 0, 1);
                        event.setEndDate(calendar);
                    }
                }
            }

            @Override
            public void addTableModelListener(TableModelListener tableModelListener) {

            }

            @Override
            public void removeTableModelListener(TableModelListener tableModelListener) {

            }
        });


        addEventButton = new JButton("Add...");
        removeEventButton = new JButton("Remove");
        editEventButton = new JButton("Edit...");


//        gc.insets = new Insets(5, 0, 5, 0);
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 1;


        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0.5;
        this.add(addEventButton, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.weightx = 0.5;
        this.add(removeEventButton, gc);

        gc.gridx = 2;
        gc.gridy = 0;
        gc.weightx = 0.5;
        this.add(editEventButton, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 3;
        gc.weightx = 0.5;
//        gc.fill = GridBagConstraints.HORIZONTAL;
//        this.add(eventTable, gc);

        eventTableContainer = new JScrollPane(eventTable);
        eventTable.setFillsViewportHeight(true);
        this.add(eventTableContainer, gc);

        eventTableContainer.setMinimumSize(new Dimension(300, 300));
    }

    public static void main(String[] args) {
        // Test harness for EventPane
        JFrame toplevel = new JFrame("eventpane test");
        toplevel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        EventPane eventPane = new EventPane();
        toplevel.add(eventPane);
        toplevel.pack();
        toplevel.setVisible(true);
    }
}
