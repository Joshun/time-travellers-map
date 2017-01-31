package org.timetravellersmap.gui;

import net.miginfocom.swing.MigLayout;
import org.geotools.map.MapContent;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.*;
import org.geotools.swing.control.JMapStatusBar;
import org.timetravellersmap.gui.eventpane.EventPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MapFrame: the GUI component in which the main GUI components reside
 * Based off Geotools' JMapFrame but customising to include timeline and other components
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

    private JMapPane mapPane;
    private JToolBar toolBar;
    private boolean uiSet;


    public MapFrame(MapContent content) {

        // Constructor code adapted from org.geotools.swing.JMapFrame example
        super(content == null ? "" : content.getTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

    public static void showMap(final MapContent content) {
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
        System.out.println("doshowmapcontent.");
        final MapFrame frame = new MapFrame(content);
        frame.initComponents();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public void initComponents() {
        System.out.println("init.");
        if (uiSet) {
            // @todo log a warning ?
            return;
        }

        /*
         * We use the MigLayout manager to make it easy to manually code
         * our UI design
         */
        StringBuilder sb = new StringBuilder();

        sb.append("[]");

        sb.append("[grow]"); // map pane and optionally layer table fill space
        sb.append("[min!]"); // status bar height

        JPanel panel = new JPanel(new MigLayout(
                "wrap 1, insets 0", // layout constrains: 1 component per row, no insets

                "[grow]", // column constraints: col grows when frame is resized

                sb.toString() ));

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

        EventPane eventPane = new EventPane();

        // Here the timeline widget will be configured
        TimelineWidget timelineWidget = new TimelineWidget(1900, 2000, 600, 50, eventPane);
        eventPane.setTimelineWidget(timelineWidget);


        panel.add(toolBar, "grow");

//        }

//        if (showLayerTable) {
//            mapLayerTable = new MapLayerTable(mapPane);
//
//            /*
//             * We put the map layer panel and the map pane into a JSplitPane
//             * so that the user can adjust their relative sizes as needed
//             * during a session. The call to setPreferredSize for the layer
//             * panel has the effect of setting the initial position of the
//             * JSplitPane divider
//             */
//            mapLayerTable.setPreferredSize(new Dimension(200, -1));
//            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
//                    false,
//                    mapLayerTable,
//                    mapPane);
//            panel.add(splitPane, "grow");
//        }

//        } else {
            /*
         * No layer table, just the map pane
         */

            /*
             * We put the map layer panel and the map pane into a JSplitPane
             * so that the user can adjust their relative sizes as needed
             * during a session. The call to setPreferredSize for the layer
             * panel has the effect of setting the initial position of the
             * JSplitPane divider
             */

        eventPane.setPreferredSize(new Dimension(100, -1));

        mapPane.setPreferredSize(new Dimension(600, -1));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                false,
                mapPane,
                eventPane);
        panel.add(splitPane, "grow");

        panel.add(JMapStatusBar.createDefaultStatusBar(mapPane), "grow");

        panel.add(timelineWidget, "grow");

        this.getContentPane().add(panel);
        uiSet = true;

        timelineWidget.setPointer(1950);
    }
}
