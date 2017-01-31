package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.Annotation;
import org.timetravellersmap.timeline.Event;
import org.timetravellersmap.timeline.EventIndex;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * EventPane: displays a list of events for the current time period
 * This will display events that start, finish or are taking place during the selected year
 */
public class EventPane extends JPanel {
    private JScrollPane eventTableContainer;
    private JTable eventTable;
    private JButton addEventButton;
    private JButton removeEventButton;
    private JButton editEventButton;
    private JButton annotateEventButton;

    private AnnotateMenu annotateMenu = new AnnotateMenu();
    private int annotateMenuSpawnX = 0;
    private int annotateMenuSpawnY = 0;

    private ArrayList<Event> currentEvents = new ArrayList<>();
    private EventIndex eventIndex = new EventIndex();
    private int pointerYear;

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
                return currentEvents.size();
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
        annotateEventButton = new JButton("Annotate...");

        EventPane parentEventPane = this;

        addEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Add event...");
                new AddModifyEventDialog(parentEventPane, eventIndex, pointerYear);
            }
        });

        removeEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Remove event...");
                Event event = getSelectedEvent();
                if (event != null) {
                    currentEvents.remove(event);
                    eventIndex.removeEvent(event);
                    eventTable.updateUI();
                }
                // TODO: implement remove event
            }
        });

        editEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Edit event...");
                Event event = getSelectedEvent();
                if (event != null) {
                    new AddModifyEventDialog(event, parentEventPane, eventIndex, pointerYear);
                }
            }
        });

        annotateEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Annotate");
                JButton btn = (JButton) actionEvent.getSource();
                int x = btn.getX();
                int y = btn.getY() + btn.getHeight();
                annotateMenu.show(parentEventPane, x, y);
            }
        });

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

        gc.gridx = 3;
        gc.gridy = 0;
        gc.weightx = 0.5;
        this.add(annotateEventButton, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 4;
        gc.weightx = 0.5;
//        gc.fill = GridBagConstraints.HORIZONTAL;
//        this.add(eventTable, gc);

        eventTableContainer = new JScrollPane(eventTable);
        eventTable.setFillsViewportHeight(true);
        this.add(eventTableContainer, gc);

        eventTableContainer.setMinimumSize(new Dimension(300, 300));

    }

    private Event getSelectedEvent() {
        int eventIndex = eventTable.getSelectedRow();
        System.out.println("index " + eventIndex);
        if (eventIndex == -1 || eventTable.getRowCount() == 0) {
            return null;
        }
        else {
            return currentEvents.get(eventIndex);
        }
    }

    public void updateExistingEvent(Event oldEvent, Event newEvent) {
        this.currentEvents.remove(oldEvent);
        this.currentEvents.add(newEvent);
        eventTable.updateUI();
    }

    public void addNewEvent(Event event) {
        this.currentEvents.add(event);
        eventTable.updateUI();
    }

    public void replaceCurrentEvents(int pointerYear) {
        this.currentEvents = eventIndex.getPointerEvents(pointerYear);
//        this.currentEvents = events;
        eventTable.updateUI();
    }

    public void setPointerYear(int pointerYear) {
        this.pointerYear = pointerYear;
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
