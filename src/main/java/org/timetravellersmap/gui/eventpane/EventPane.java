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

import org.geotools.swing.JMapPane;
import org.timetravellersmap.core.Descriptor;
import org.timetravellersmap.core.event.EventChangeListener;
import org.timetravellersmap.core.event.EventSelectChangeListener;
import org.timetravellersmap.core.timeline.TimelineChangeListener;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.core.event.Event;
import org.timetravellersmap.overlay.LayerComponentChangeListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

/**
 * EventPane: displays a list of events for the current time period
 * This will display events that start, finish or are taking place during the selected year
 */
public class EventPane extends JPanel implements TimelineChangeListener, LayerComponentChangeListener {
    private final static Logger LOGGER = Logger.getLogger(EventPane.class.getName());
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

//        currentEvents.add(new Event(
//                new GregorianCalendar(1900, 0, 1),
//                new GregorianCalendar(1910, 0, 1),
//                new Descriptor("Test event", "This is a description")
//        ));

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
                        return event.getEventDescriptor().getName();
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
                    Descriptor oldDescriptor = event.getEventDescriptor();

                    if (i1 == 0) {
                        String name = oldDescriptor.getName();
                        String description = oldDescriptor.getDescription();
                        Descriptor newDescriptor = new Descriptor(name, description);
                        event.setEventDescriptor(newDescriptor);
                    }
                    else if (i1 == 1) {
                        GregorianCalendar calendar = new GregorianCalendar((int)o, 0, 1);
                        event.setStartDate(calendar);
                    }
                    else if (i1 == 2) {
                        GregorianCalendar calendar = new GregorianCalendar((int)o, 0, 1);
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

        eventTable.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                int row = eventTable.rowAtPoint(mouseEvent.getPoint());
                if (row > -1) {
                    Event event = currentEvents.get(row);
                    eventTable.setToolTipText(event.getTooltipText());
                }
                else {
                    eventTable.setToolTipText(null);
                }
            }
        });

        eventTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                LOGGER.info("select " + listSelectionEvent);
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

        EventPane parentEventPane = this;

        addEventButton.addActionListener(actionEvent ->  {
            eventSelected = false;
            LOGGER.info("Add event...");
            new AddModifyEventDialog(mapFrame, parentEventPane, timelinePointerYear);
        });

        removeEventButton.addActionListener(actionEvent ->  {
            LOGGER.info("Remove event...");
            Event event = getSelectedEvent();
            if (event != null) {
                currentEvents.remove(event);
                mapFrame.removeEventFromIndex(event);
                eventTable.repaint();
                setContextDependentButtonsEnabled(false);
                fireSelectChangeListenersDeselect();
                eventTable.clearSelection();
                eventSelected = false;
                fireChangeListeners();
            }
        });

        editEventButton.addActionListener(actionEvent ->  {
            Event event = getSelectedEvent();
            if (event != null) {
                new AddModifyEventDialog(event, mapFrame, parentEventPane, timelinePointerYear);
            }
        });

        toggleAnnotationButton.addActionListener(actionEvent -> {
            mapFrame.getAnnotatePane().toggleVisibleState();
        });

        showLayerManagerButton.addActionListener(actionEvent -> {
            LayerManager layerManager =  new LayerManager(mapFrame.getLayerList(), mapFrame);
            layerManager.addLayerChangeListener(mapFrame.getAnnotatePane());
            layerManager.setVisible(true);
        });

        // Begin layout of GUI components
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
        // End layout of GUI components

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
        editEventButton.setEnabled(enabled);
        removeEventButton.setEnabled(enabled);
        toggleAnnotationButton.setEnabled(enabled);
    }

    public Event getSelectedEvent() {
        int eventReference = eventTable.getSelectedRow();
        LOGGER.info("index " + eventReference);
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
        eventTable.repaint();
    }

    public void addNewEvent(Event event) {
        this.currentEvents.add(event);
        eventTable.repaint();

    }

    public void timelineChanged(int year, boolean redraw) {
        // Deselect event selection
        if (eventSelected) {
            fireSelectChangeListenersDeselect();
            eventTable.clearSelection();
            eventSelected = false;
        }
        setContextDependentButtonsEnabled(false);
        replaceCurrentEvents(year);
        if (redraw) {
            mapFrame.getLayerList().setEventsToDraw(currentEvents);
        }
    }

    private void replaceCurrentEvents(int pointerYear) {
        this.currentEvents = mapFrame.getEventIndex().getPointerEvents(pointerYear);
        timelinePointerYear = pointerYear;
        eventTable.repaint();
    }

    public void layerComponentChanged() {
        LOGGER.info("layerComponentChanged called");
        replaceCurrentEvents(mapFrame.getTimelineWidget().getPointerYear());
        mapFrame.getLayerList().setEventsToDraw(currentEvents);
        // Try to prevent flickering
        ((JMapPane)mapFrame.getMapPane()).repaint();
    }

    public ArrayList<Event> getCurrentEvents() {
        return currentEvents;
    }

    public void setSelectedEvent(Event event) {
        if (event == null ) {
             return;
        }
        eventTable.getSelectionModel().setSelectionInterval(currentEvents.indexOf(event), currentEvents.indexOf(event));
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
