package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.Annotation;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.gui.TimelineWidget;
import org.timetravellersmap.timeline.*;
import org.timetravellersmap.timeline.Event;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
public class EventPane extends JPanel implements TimelineChangeListener {
    private JScrollPane eventTableContainer;
    private JTable eventTable;
    private JButton addEventButton;
    private JButton removeEventButton;
    private JButton editEventButton;
//    private JButton annotateEventButton;
    private JButton toggleAnnotationButton;
    private JButton showLayerManagerButton;

    private boolean eventSelected = false;

    private ArrayList<EventChangeListener> changeListeners = new ArrayList<>();
    private ArrayList<EventSelectChangeListener> selectChangeListeners = new ArrayList<>();

//    private AnnotateMenu annotateMenu = new AnnotateMenu();

    private ArrayList<Event> currentEvents = new ArrayList<>();

    private MapFrame mapFrame = null;

    private int timelinePointerYear;


    public EventPane(MapFrame parentMapFrame) {
        this.mapFrame = parentMapFrame;

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        String[] eventTableColumns = new String[] { "Event", "Start", "End" };

        timelinePointerYear = parentMapFrame.INITIAL_START_YEAR;

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

        eventTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                System.out.println("select " + listSelectionEvent);
                if (eventTable.getRowCount() > 0) {
                    setContextDependentButtonsEnabled(true);
                    fireSelectChangeListenersSelect(getSelectedEvent());
                    eventSelected = true;
                }
            }
        });


        addEventButton = new JButton("Add...");
        removeEventButton = new JButton("Remove");
        editEventButton = new JButton("Edit...");
        toggleAnnotationButton = new JButton("Annotations...");
        showLayerManagerButton = new JButton("Layers...");
//        annotateEventButton = new JButton("Annotate...");

        EventPane parentEventPane = this;

        addEventButton.addActionListener(actionEvent ->  {
            eventSelected = false;
            System.out.println("Add event...");
            new AddModifyEventDialog(mapFrame, parentEventPane, timelinePointerYear);
        });

        removeEventButton.addActionListener(actionEvent ->  {
            System.out.println("Remove event...");
            Event event = getSelectedEvent();
            if (event != null) {
                currentEvents.remove(event);
                mapFrame.removeEventFromIndex(event);
                eventTable.updateUI();
                setContextDependentButtonsEnabled(false);
                fireSelectChangeListenersDeselect();
                eventTable.clearSelection();
                eventSelected = false;
//                mapFrame.redrawTimeline();
                fireChangeListeners();
            }
            // TODO: implement remove event
        });

        editEventButton.addActionListener(actionEvent ->  {
            System.out.println("Edit event...");
            Event event = getSelectedEvent();
            if (event != null) {
                new AddModifyEventDialog(event, mapFrame, parentEventPane, timelinePointerYear);
//                addModifyEventDialog.addChangeListener(mapFrame.getTimelineWidget());
            }
        });

        toggleAnnotationButton.addActionListener(actionEvent -> {
//            mapFrame.getAnnotatePane().toggleVisibleState();
            mapFrame.toggleAnnotatePane();
            if (!mapFrame.getAnnotatePane().isVisible()) {
                eventSelected = false;
            }
        });

        showLayerManagerButton.addActionListener(actionEvent -> {
            new LayerManager(mapFrame.getLayerList()).setVisible(true);
        });

//        annotateEventButton.addActionListener(actionEvent ->  {
//            System.out.println("Annotate");
//            Event event = getSelectedEvent();
//            if (event != null) {
//                JButton btn = (JButton) actionEvent.getSource();
//                // Spawn popup annotate menu underneath button
//                int x = btn.getX();
//                int y = btn.getY() + btn.getHeight();
//                annotateMenu.show(parentEventPane, x, y);
//            }
//        });

//        gc.insets = new Insets(5, 0, 5, 0);
        gc.anchor = GridBagConstraints.PAGE_START;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 1;


        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        this.add(addEventButton, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        this.add(removeEventButton, gc);

        gc.gridx = 2;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        this.add(editEventButton, gc);

//        gc.gridx = 3;
//        gc.gridy = 0;
//        gc.weightx = 0.5;
//        this.add(annotateEventButton, gc);

        gc.gridx = 3;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weighty = 0.1;
        this.add(toggleAnnotationButton, gc);

        gc.gridx = 4;
        gc.gridy = 0;
        gc.weightx = 0.5;
        gc.weightx = 0.1;
        this.add(showLayerManagerButton, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 5;
        gc.weightx = 0.5;
        gc.weighty = 0.7;
        eventTableContainer = new JScrollPane(eventTable);
        eventTable.setFillsViewportHeight(true);
        this.add(eventTableContainer, gc);

        eventTableContainer.setMinimumSize(new Dimension(300, 300));
        setContextDependentButtonsEnabled(false);

    }

    public void addChangeListener(EventChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    public void removeChangeListener(EventChangeListener changeListener) {
        changeListeners.remove(changeListener);
    }

    public ArrayList<EventChangeListener> getChangeListeners() {
        return changeListeners;
    }

    private void fireChangeListeners() {
        for (EventChangeListener changeListener: changeListeners) {
            changeListener.eventChanged();
        }
    }

    public void addSelectChangeListener(EventSelectChangeListener selectChangeListener) {
        selectChangeListeners.add(selectChangeListener);
    }

    public void removeSelectChangeListener(EventSelectChangeListener selectChangeListener) {
        selectChangeListeners.remove(selectChangeListener);
    }

    public ArrayList<EventSelectChangeListener> getSelectChangeListeners() {
        return selectChangeListeners;
    }

    public void fireSelectChangeListenersSelect(Event event) {
        for (EventSelectChangeListener eventSelectChangeListener: selectChangeListeners) {
            eventSelectChangeListener.eventSelected(event);
        }
    }

    public void fireSelectChangeListenersDeselect() {
        for (EventSelectChangeListener eventSelectChangeListener: selectChangeListeners) {
            eventSelectChangeListener.eventDeselected();
        }
    }

    private void setContextDependentButtonsEnabled(boolean enabled) {
        System.out.println("toggle");
        editEventButton.setEnabled(enabled);
        removeEventButton.setEnabled(enabled);
        toggleAnnotationButton.setEnabled(enabled);
    }

    public Event getSelectedEvent() {
        int eventReference = eventTable.getSelectedRow();
        System.out.println("index " + eventReference);
        if (eventReference == -1 || eventTable.getRowCount() == 0) {
            return null;
        }
        else {
            return currentEvents.get(eventReference);
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

    public void timelineChanged(int year) {
        if (eventSelected) {
            fireSelectChangeListenersDeselect();
            eventTable.clearSelection();
            eventSelected = false;
        }
        setContextDependentButtonsEnabled(false);
        System.out.println("Changing to year " + year + "...");
        replaceCurrentEvents(year);
    }

    private void replaceCurrentEvents(int pointerYear) {
        this.currentEvents = mapFrame.getEventIndex().getPointerEvents(pointerYear);
        timelinePointerYear = pointerYear;
        eventTable.updateUI();
    }


    public static void main(String[] args) {
        // Test harness for EventPane
//        JFrame toplevel = new JFrame("eventpane test");
//        toplevel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        EventPane eventPane = new EventPane();
//        toplevel.add(eventPane);
//        toplevel.pack();
//        toplevel.setVisible(true);
    }
}
