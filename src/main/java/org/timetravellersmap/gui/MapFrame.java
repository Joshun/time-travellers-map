package org.timetravellersmap.gui;

import net.miginfocom.swing.MigLayout;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapPane;
import org.geotools.swing.MapPane;
import org.geotools.swing.action.*;
import org.geotools.swing.control.JMapStatusBar;
import org.timetravellersmap.ShapefileException;
import org.timetravellersmap.TimeTravellersMapException;
import org.timetravellersmap.gui.annotatepane.AnnotatePane;
import org.timetravellersmap.gui.eventpane.EventPane;
import org.timetravellersmap.jsonio.JsonIO;
import org.timetravellersmap.jsonio.JsonIOObject;
import org.timetravellersmap.overlay.LayerList;
import org.timetravellersmap.core.event.EventIndex;
import org.timetravellersmap.core.event.Event;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

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

    public static final boolean UPDATE_MAP_ON_DRAG = false;

    private static final String JSON_FILE_NAME = "ttm_state.json";

    private JMapPane mapPane;
    private JToolBar toolBar;
    private boolean uiSet;

    private EventIndex eventIndex = new EventIndex();
    private LayerList layerList;
    private EventPane eventPane;
    private AnnotatePane annotatePane;
    private JSplitPane eventAnnotateSplitPane;
    private TimelineWidget timelineWidget;

    private JsonIO jsonIO = new JsonIO();
    private JsonIOObject jsonIOObject;

    private Layer baseLayer = null;

    private MapContent mapContent;

    public MapFrame(MapContent content, Layer baseLayer) throws TimeTravellersMapException {
        super(content == null ? "" : content.getTitle());
        this.mapContent = content;
        this.baseLayer = baseLayer;
        if (baseLayer == null) {
            throw new TimeTravellersMapException("Must set a baselayer with MapFrame.setBaseLayer");
        }

        // Constructor code adapted from org.geotools.swing.JMapFrame example
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layerList = new LayerList(content, baseLayer);

        // If ttm_state file already exists, load the state from it
        if (jsonStateFileExists()) {
            loadStateFromJson();
        }

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

    public void showMap()  {
        if (SwingUtilities.isEventDispatchThread()) {
            doShowMap();
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    doShowMap();
                }
            });
        }
    }

    private void doShowMap() {
//        try {
        System.out.println("doshowmapcontent.");
        initComponents();
        setSize(1024, 800);
        setVisible(true);
//            final MapFrame frame = new MapFrame();
//            frame.initComponents();
//            frame.setSize(1024, 800);
//            frame.setVisible(true);
//        }
//        catch (TimeTravellersMapException e) {
//            System.out.println("Failed " + e);
//        }
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

        btn = new JButton(new SaveAction(mapPane, this));
        btn.setName("Save");
        toolBar.add(btn);

        btn = new JButton(new LoadAction(mapPane, this));
        btn.setName("Load");
        toolBar.add(btn);

        eventPane = new EventPane(this);

        // Here the core widget will be configured
        timelineWidget = new TimelineWidget(1900, 2000, 600, 50, this);

        panel.add(toolBar, "grow");

        eventPane.setPreferredSize(new Dimension(100, 300));

        timelineWidget.addChangeListener(eventPane);
        eventPane.addChangeListener(timelineWidget);

        mapPane.setPreferredSize(new Dimension(600, -1));

        annotatePane = new AnnotatePane(this);
        eventPane.addSelectChangeListener(annotatePane);

        annotatePane.setPreferredSize(new Dimension(100, 300));
        eventAnnotateSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                false,
                eventPane,
                annotatePane);
        annotatePane.setVisible(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                false,
                mapPane,
                eventAnnotateSplitPane);
        splitPane.setDividerLocation(0.7);
        panel.add(splitPane, "grow");

        panel.add(JMapStatusBar.createDefaultStatusBar(mapPane), "grow");

        panel.add(timelineWidget, "grow");

        this.getContentPane().add(panel);
        uiSet = true;

        // Set up JsonIOObject for saving if not already created
        if (jsonIOObject == null) {
            jsonIOObject = new JsonIOObject(layerList, eventIndex);
        }

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // Save state on exit
                System.out.println("Quit signal received, saving...");
                saveStateToJson();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });

        layerList.setMapContent(getMapPane().getMapContent());

        // Set pointer to initial position
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

    public void setBaseLayer(org.geotools.map.Layer baseLayer) {
        this.baseLayer = baseLayer;
    }

    public void repaintMapContent() {
        mapPane.getMapContent().getViewport().setBounds(new ReferencedEnvelope(
                -180.0,
                180.0,
                -90.0,
                90.0,
                DefaultGeographicCRS.WGS84
        ));
    }

    public MapPane getMapPane() {
        return mapPane;
    }

    public void refreshMapPane() {
        timelineWidget.redraw();
        eventPane.timelineChanged(timelineWidget.getPointerYear(), true);
        annotatePane.annotationsChanged();
    }

    public void loadStateOnTheFly() {
        loadStateFromJson();
        File shapeFile = new File("/home/joshua/Documents/Computer Science/Dissertation Project/sample data/ne_50m_admin_0_sovereignty.shp");
        SimpleFeatureSource featureSource;
        FileDataStore shapeFileStore;
        Style style;
        Layer layer;

        try {
            shapeFileStore = FileDataStoreFinder.getDataStore(shapeFile);
            featureSource = shapeFileStore.getFeatureSource();
            style = SLD.createSimpleStyle(featureSource.getSchema());
            layer = new FeatureLayer(featureSource, style);

            MapContent mapContent = new MapContent();
            mapContent.setTitle("Time Traveller's Map");
//            mapPane.setMapContent(mapContent);
//            repaintMapContent();
//            mapPane.repaint();
            layerList.setMapContent(mapContent);
//            layerList.updateMapContent();
//            refreshMapPane();

            setBaseLayer(layer);
//            mapPane.setMapContent(mapContent);
            mapPane = new JMapPane(mapContent);
        }
        catch (java.io.IOException e) {
            System.out.println("Error loading shapefile.");
        }


    }

    public File pickLoadStateFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showDialog(this, "Load");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        else {
            return null;
        }
    }

    public void saveStateToJson() {
        if (jsonIOObject != null && jsonIOObject.isReady()) {
            jsonIO.saveJson(JSON_FILE_NAME, jsonIOObject);
        }
    }

    public void loadStateFromJson() {
        JsonIOObject jsonIOObject = jsonIO.loadJson(JSON_FILE_NAME);
        layerList = jsonIOObject.getLayerList();
        eventIndex = jsonIOObject.getEventIndex();
    }

    public boolean jsonStateFileExists() {
        return new File(JSON_FILE_NAME).exists();
    }
}
