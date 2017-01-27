package org.timetravellersmap.gui;

import org.timetravellersmap.timeline.Event;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by joshua on 27/01/17.
 */
public class EventPane extends JPanel {
    private JTable eventTable;
    private JButton addEventButton;
    private JButton removeEventButton;

    private ArrayList<Event> currentEvents;

    public EventPane() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        String[] eventTableColumns = new String[] { "Event", "Start", "End" };
        eventTable = new JTable(new TableModel() {
            @Override
            public int getRowCount() {
                return currentEvents.size();
            }

            @Override
            public int getColumnCount() {
                return eventTableColumns.length;
            }

            @Override
            public String getColumnName(int i) {
                if (i < eventTableColumns.length) {
                    return eventTableColumns[i];
                }
                else {
                    return null;
                }
            }

            @Override
            public Class<?> getColumnClass(int i) {
                return null;
            }

            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }

            @Override
            public Object getValueAt(int i, int i1) {
                Event event = currentEvents.get(i);
                if (i1 == 0) {
                    return event.getEventAnnotation().getName();
                }
                else if (i1 == 1) {
                    return event.getStartDateAsYear();
                }
                else if (i1 == 2) {
                    return event.getEndDateAsYear();
                }
                else {
                    return null;
                }
            }

            @Override
            public void setValueAt(Object o, int i, int i1) {

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

        gc.gridx = 0;
        gc.gridy = 0;
        this.add(addEventButton);

        gc.gridx = 1;
        gc.gridy = 0;
        this.add(removeEventButton);

        gc.gridx = 0;
        gc.gridy = 1;
        this.add(eventTable);
    }
}