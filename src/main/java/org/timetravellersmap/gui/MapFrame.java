package org.timetravellersmap.gui;

import net.miginfocom.swing.MigLayout;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapPane;
import org.geotools.swing.MapPane;
import org.geotools.swing.action.*;
import org.geotools.swing.control.JMapStatusBar;
import org.timetravellersmap.ShapefileException;
import org.timetravellersmap.TimeTravellersMapException;
import org.timetravellersmap.basemapio.BasemapIOException;
import org.timetravellersmap.basemapio.BasemapLoader;
import org.timetravellersmap.basemapio.BasemapLoaderFactory;
import org.timetravellersmap.core.Basemap;
import org.timetravellersmap.core.BasemapList;
import org.timetravellersmap.core.timeline.TimelineChangeListener;
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
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * MapFrame: the GUI component in which the main GUI components reside
 * Based off Geotools' JMapFrame but customising to include core and other components
 */
public class MapFrame extends JFrame implements TimelineChangeListener{
    private static final Logger LOGGER = Logger.getLogger(MapFrame.class.getName());
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

//    private final String JSON_FILE_NAME;
    private String jsonFileName;

    private JMapPane mapPane;
    private JToolBar toolBar;
    private boolean uiSet;

    private EventIndex eventIndex = new EventIndex();
    private LayerList layerList;
    private BasemapList basemapList = new BasemapList();
    private EventPane eventPane;
    private AnnotatePane annotatePane;
    private JSplitPane eventAnnotateSplitPane;
    private TimelineWidget timelineWidget;
    private StatusBar statusBar = new StatusBar(this, 600, 200);

    private JsonIO jsonIO = new JsonIO();
    private JsonIOObject jsonIOObject;

    private Layer baseLayer = null;

    private MapContent mapContent;

    private Basemap currentBasemap;
    private ArrayList<BasemapChangeListener> basemapChangeListeners = new ArrayList<>();

    public void addBasemapChangeListener(BasemapChangeListener basemapChangeListener) {
        basemapChangeListeners.add(basemapChangeListener);
    }

    public void removeBasemapChangeListener(BasemapChangeListener basemapChangeListener) {
        basemapChangeListeners.remove(basemapChangeListener);
    }

    private void fireBasemapExpired() {
        for (BasemapChangeListener changeListener: basemapChangeListeners) {
            changeListener.basemapExpired();
        }
    }

    private void fireBasemapChanged(Basemap basemap, boolean isValid) {
        for (BasemapChangeListener changeListener: basemapChangeListeners) {
            changeListener.basemapChanged(basemap, isValid);
        }
    }


//    public MapFrame(MapContent content, Layer baseLayer) throws TimeTravellersMapException {
    public MapFrame(MapContent content) throws TimeTravellersMapException {
        super(content == null ? "" : content.getTitle());
        this.mapContent = content;
//        this.JSON_FILE_NAME = jsonFilename;
//        this.baseLayer = baseLayer;
//        if (baseLayer == null) {
//            throw new TimeTravellersMapException("Must set a baselayer with MapFrame.setBaseLayer");
//        }

        // Constructor code adapted from org.geotools.swing.JMapFrame example
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layerList = new LayerList(content, baseLayer);

        // If ttm_state file already exists, load the state from it
//        if (jsonStateFileExists()) {
//            loadStateFromJson();
//        }

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

    private boolean hasValidFilenameExtension(String f) {
        int i = f.lastIndexOf(".");
        return i > 0 && i+1 < f.length()-1 && f.substring(i+1).equals("ttm");
    }

    private void doShowMap() {
//        try {
        if (new File(jsonFileName).exists()) {
            loadStateFromJson();
        }
        else {
            System.out.println("hasValidFileName " + hasValidFilenameExtension(jsonFileName));
            if (!hasValidFilenameExtension(jsonFileName)) {
                jsonFileName = jsonFileName + ".ttm";
            }
        }
        setTitle("Time Traveller's Map - " + new File(jsonFileName).getName());
        LOGGER.info("doshowmapcontent.");
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
        LOGGER.info("init.");
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

        toolBar.addSeparator();

        btn = new JButton(new SaveAction(mapPane, this));
        toolBar.add(btn);

//        btn = new JButton(new LoadAction(mapPane, this));
//        toolBar.add(btn);

        btn = new JButton(new BasemapManagerAction(this, basemapList));
        toolBar.add(btn);

        btn = new JButton(new TimeTravelAction(this));
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
        splitPane.setDividerLocation(0.9);
        panel.add(splitPane, "grow");

        panel.add(JMapStatusBar.createDefaultStatusBar(mapPane), "grow");

        panel.add(timelineWidget, "grow");

        panel.add(statusBar, "grow");

        this.getContentPane().add(panel);
        uiSet = true;

        // Set up JsonIOObject for saving if not already created
        if (jsonIOObject == null) {
            jsonIOObject = new JsonIOObject(layerList, eventIndex, basemapList);
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // Save state on exit
                LOGGER.info("Quit signal received, saving...");
                saveStateToJson();
            }
        });

        layerList.setMapContent(getMapPane().getMapContent());

        // Set pointer to initial position
        timelineWidget.addChangeListener(this);
        eventPane.addChangeListener(statusBar);
        statusBar.setEventCountStatus(eventIndex.getTotalEvents());
        addBasemapChangeListener(statusBar);

        timelineWidget.setPointer(1950);
        timelineChanged(1950, true);

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

    public BasemapList getBasemapList() {
        return basemapList;
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
            layerList.setMapContent(mapContent);

            setBaseLayer(layer);
            mapPane = new JMapPane(mapContent);
        }
        catch (java.io.IOException e) {
            LOGGER.warning("Error loading shapefile.");
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
            jsonIO.saveJson(jsonFileName, jsonIOObject);
        }
    }

    public void loadStateFromJson() {
        JsonIOObject jsonIOObject = jsonIO.loadJson(jsonFileName);
        layerList = jsonIOObject.getLayerList();
        eventIndex = jsonIOObject.getEventIndex();
        basemapList = jsonIOObject.getBasemapList();
        if (basemapList == null) {
            basemapList = new BasemapList();
        }
    }

    public boolean jsonStateFileExists() {
        return new File(jsonFileName).exists();
    }

    public void setJsonFileName(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }


    public void timelineChanged(int year, boolean redraw) {
        boolean basemapExpired = BasemapList.basemapExpired(currentBasemap, timelineWidget.getPointerYear());
        LOGGER.info("Timeline changed, basemap expired? " + basemapExpired);
        if (basemapExpired) {
            fireBasemapExpired();
//            Basemap basemap = basemapList.getForYears(timelineWidget.getStart(), timelineWidget.getPointerYear());
            Basemap basemap = basemapList.getFromPointer(timelineWidget.getPointerYear());
            LOGGER.info("Attempting to load basemap " + basemap);
            if (basemap != null ) {
                mapContent.dispose();
                mapContent = new MapContent();
                layerList.setMapContent(mapContent);
                currentBasemap = basemap;
                loadBasemapFile(new File(basemap.getFilePath()));
                fireBasemapChanged(currentBasemap, true);
                mapPane.setMapContent(mapContent);
            }
        }
        else {
            fireBasemapChanged(currentBasemap, true);
        }
    }

    public StatusBar getStatusBar() {
        return statusBar;
    }

    private void loadBasemapFile(File file) {
<<<<<<< ddcd7275aeb9f5bfb9c42d4fd6dcaa2853bdd07e
<<<<<<< e7ffffc11336bd16ae9dca367febf1590dedc532
        String fileExt = BasemapLoaderFactory.getExtension(file.getName());
=======
        int extPos = file.getName().lastIndexOf(".") + 1;
        String fileExt = file.getName().substring(extPos);
>>>>>>> Improve handling of different basemap file formats
=======
        String fileExt = BasemapLoaderFactory.getExtension(file.getName());
>>>>>>> Add support for tiff and giff formats
        try {
            BasemapLoader loader = BasemapLoaderFactory.basemapLoaderFromFile(fileExt);
            baseLayer = loader.loadBasemap(file);
            layerList.setBaseLayer(baseLayer);
            LOGGER.info("Loaded basemap " + file.getName());
        }
        catch (BasemapIOException e) {
            LOGGER.warning("Failed to load basemap " + file.getName());
            e.printStackTrace();
        }
    }
}
