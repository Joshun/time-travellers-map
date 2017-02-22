package org.timetravellersmap.gui;

import net.miginfocom.swing.MigLayout;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.*;
import org.geotools.swing.control.JMapStatusBar;
import org.timetravellersmap.TimeTravellersMapException;
import org.timetravellersmap.gui.annotatepane.AnnotatePane;
import org.timetravellersmap.gui.eventpane.EventPane;
import org.timetravellersmap.overlay.LayerList;
import org.timetravellersmap.core.event.EventIndex;
import org.timetravellersmap.core.event.Event;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MapFrame: the GUI component in which the main GUI components reside
 * Based off Geotools' JMapFrame but customising to include core and other components
 */
public class MapFrame extends JFrame {
    /** Name assigned to toolbar button for feature info queries. */
    private static final String TOOLBAR_INFO_BUTTON_NAME = "ToolbarInfoButton";
    /** Name assigned to toolbar button for map panning. */
    private static final String TOOLBAR_PAN_BUTTON_NAME = "ToolbarPanButton";
    /** Name assigned to toolbar button for default pointer. */
    private static final String TOOLBAR_POINTER_BUTTON_NAME = "ToolbarPointerButton";
    /** Name assigned to toolbar button for map reset. */
    private static final String TOOLBAR_RESET_BUTTON_NAME = "ToolbarResetButton";
    /** Name assigned to toolbar button for map zoom in. */
    private static final String TOOLBAR_ZOOMIN_BUTTON_NAME = "ToolbarZoomInButton";
    /** Name assigned to toolbar button for map zoom out. */
    private static final String TOOLBAR_ZOOMOUT_BUTTON_NAME = "ToolbarZoomOutButton";

    public static final int INITIAL_START_YEAR = 1900;
    public static final int INITIAL_END_YEAR = 2000;

    private JMapPane mapPane;
    private JToolBar toolBar;
    private boolean uiSet;

    private EventIndex eventIndex = new EventIndex();
    private LayerList layerList;
    private EventPane eventPane;
    private AnnotatePane annotatePane;
    private JSplitPane eventAnnotateSplitPane;
    private TimelineWidget timelineWidget;

    private static Layer baseLayer = null;

    public MapFrame(MapContent content) throws TimeTravellersMapException {
        super(content == null ? "" : content.getTitle());
        if (baseLayer == null) {
            throw new TimeTravellersMapException("Must set a baselayer with MapFrame.setBaseLayer");
        }

        // Constructor code adapted from org.geotools.swing.JMapFrame example
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layerList = new LayerList(content, baseLayer);

//        showStatusBar = false;
//        showToolBar = false;

        // the map pane is the one element that is always displayed
        mapPane = new JMapPane(content);
        mapPane.setBackground(Color.WHITE);
        mapPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // give keyboard focus to the map pane
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                mapPane.requestFocusInWindow();
            }
        });

        mapPane.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                mapPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }

            @Override
            public void focusLost(FocusEvent e) {
                mapPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
        });

        mapPane.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                mapPane.requestFocusInWindow();
            }
        });
    }

    public static void showMap(final MapContent content)  {
        if (SwingUtilities.isEventDispatchThread()) {
            doShowMap(content);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    doShowMap(content);
                }
            });
        }
    }

    private static void doShowMap(MapContent content) {
        try {
            System.out.println("doshowmapcontent.");
            final MapFrame frame = new MapFrame(content);
            frame.initComponents();
//            frame.setSize(1024, 800);
            frame.setVisible(true);
        }
        catch (TimeTravellersMapException e) {
            System.out.println("Failed " + e);
        }
    }

    public void initComponents() {
        System.out.println("init.");
        if (uiSet) {
            // @todo log a warning ?
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 0.5;
        gc.weighty = 0.5;


        /*
         * A toolbar with buttons for zooming in, zooming out,
         * panning, and resetting the map to its full extent.
         * The cursor tool buttons (zooming and panning) are put
         * in a ButtonGroup.
         *
         * Note the use of the XXXAction objects which makes constructing
         * the tool bar buttons very simple.
         */
        toolBar = new JToolBar();
        toolBar.setOrientation(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);

        JButton btn;
        ButtonGroup cursorToolGrp = new ButtonGroup();

        btn = new JButton(new NoToolAction(mapPane));
        btn.setName(TOOLBAR_POINTER_BUTTON_NAME);
        toolBar.add(btn);
        cursorToolGrp.add(btn);

        btn = new JButton(new ZoomInAction(mapPane));
        btn.setName(TOOLBAR_ZOOMIN_BUTTON_NAME);
        toolBar.add(btn);
        cursorToolGrp.add(btn);

        btn = new JButton(new ZoomOutAction(mapPane));
        btn.setName(TOOLBAR_ZOOMOUT_BUTTON_NAME);
        toolBar.add(btn);
        cursorToolGrp.add(btn);

        toolBar.addSeparator();

        btn = new JButton(new PanAction(mapPane));
        btn.setName(TOOLBAR_PAN_BUTTON_NAME);
        toolBar.add(btn);
        cursorToolGrp.add(btn);

        toolBar.addSeparator();

        btn = new JButton(new InfoAction(mapPane));
        btn.setName(TOOLBAR_INFO_BUTTON_NAME);
        toolBar.add(btn);

        toolBar.addSeparator();

        btn = new JButton(new ResetAction(mapPane));
        btn.setName(TOOLBAR_RESET_BUTTON_NAME);
        toolBar.add(btn);

        eventPane = new EventPane(this);

        // Here the core widget will be configured
        timelineWidget = new TimelineWidget(1900, 2000, 600, 50, this);

        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        panel.add(toolBar, gc);

//        eventPane.setPreferredSize(new Dimension(100, 300));

        timelineWidget.addChangeListener(eventPane);
        eventPane.addChangeListener(timelineWidget);

//        mapPane.setPreferredSize(new Dimension(600, -1));
        mapPane.setMinimumSize(new Dimension(1000, 1000));

        annotatePane = new AnnotatePane(this);
        eventPane.addSelectChangeListener(annotatePane);

//        annotatePane.setPreferredSize(new Dimension(100, 300));
        eventAnnotateSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                false,
                eventPane,
                annotatePane);
        annotatePane.setVisible(false);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                false,
                mapPane,
                eventAnnotateSplitPane);
//        panel.add(splitPane, "grow");
        panel.add(splitPane, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 1;
//        panel.add(JMapStatusBar.createDefaultStatusBar(mapPane), "grow");
        panel.add(JMapStatusBar.createDefaultStatusBar(mapPane), gc);


        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 1;
        panel.add(timelineWidget, gc);

        this.getContentPane().add(panel);

        mapPane.setSize(new Dimension(1000, 1000));
        mapPane.revalidate();
        splitPane.setDividerLocation(0.7);


        pack();

        uiSet = true;

        timelineWidget.setPointer(1950);
    }

    public EventIndex getEventIndex() {
        return eventIndex;
    }

    public EventPane getEventPane() {
        return eventPane;
    }

    public TimelineWidget getTimelineWidget() {
        return timelineWidget;
    }

    public AnnotatePane getAnnotatePane() {
        return annotatePane;
    }

    public LayerList getLayerList() {
        return layerList;
    }


    public void removeEventFromIndex(Event e) {
        eventIndex.removeEvent(e);
    }

    public void changeAnnotateDividerState(boolean shown) {
        if (shown) {
            eventAnnotateSplitPane.setDividerLocation(300);
        }
        else {
            eventAnnotateSplitPane.setDividerLocation(600);
        }
    }

    public static void setBaseLayer(org.geotools.map.Layer baseLayer) {
        MapFrame.baseLayer = baseLayer;
    }

}
