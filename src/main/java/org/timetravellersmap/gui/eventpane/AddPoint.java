package org.timetravellersmap.gui.eventpane;

import org.geotools.map.MapViewport;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.event.MapMouseListener;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.gui.annotatepane.AnnotatePane;
import org.timetravellersmap.gui.annotatepane.ColorChangeListener;
import org.timetravellersmap.gui.annotatepane.ColorPanel;
import org.timetravellersmap.gui.annotatepane.ColorPicker;
import org.timetravellersmap.overlay.LayerComponent;
import org.timetravellersmap.overlay.PointComponent;
import org.timetravellersmap.core.event.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * AddPoint: a GUI for the user to add a PointComponent to an Event
 */
public class AddPoint extends AddComponent implements ColorChangeListener {
    private JTextField longitudeEntry = new JTextField(10);
    private JTextField latitudeEntry = new JTextField(10);
    private JSpinner radiusEntry = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
    private JButton pickColorButton = new JButton("Pick colour...");
    private ColorPanel colorPanel = new ColorPanel(16, 16, new Color(0, 0, 0));

    private static final String GET_LONG_LAT_FROM_MAP_BUTTON_INITIAL_TEXT = "Get coordinates from map...";
    private static final String GET_LONG_LAT_FROM_MAP_BUTTON_CLICKED_TEXT = "(click map)";
    private JButton getLongLatFromMapButton = new JButton(GET_LONG_LAT_FROM_MAP_BUTTON_INITIAL_TEXT);
    private boolean inGetLongLatFromMapState = false;
    private int mouseX = 0;
    private int mouseY = 0;

    private Color colorState = new Color(0,0,0);

//    private JButton addPointButton = new JButton("Add point");
//    private JButton cancelButton = new JButton("Cancel");

//    private LayerManager layerManager;

    private NameAndDescriptionInput nameAndDescriptionInput = new NameAndDescriptionInput(this);

    private MapFrame mapFrame;
//    private AnnotatePane annotatePane;
    private Event event;

    public AddPoint(MapFrame ancestorMapFrame, AnnotatePane annotatePane, Event event) {
        super(annotatePane, event);
        this.mapFrame = ancestorMapFrame;
//        setUpPanel();
        this.annotatePane = annotatePane;
        this.event = annotatePane.getSelectedEvent();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        // Begin setting up action listeners
        getLongLatFromMapButton.addActionListener(actionEvent -> {
            inGetLongLatFromMapState = true;
            getLongLatFromMapButton.setText(GET_LONG_LAT_FROM_MAP_BUTTON_CLICKED_TEXT);

            org.geotools.swing.event.MapMouseListener mouseListener = new org.geotools.swing.event.MapMouseListener() {
                public void onMousePressed(MapMouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();

                    Point2D.Double screenCoords = new Point2D.Double(mouseX, mouseY);
                    Point2D.Double worldCoords = screenToWorld(screenCoords, mapFrame.getMapPane().getMapContent().getViewport());
                    Double worldCoordsLong = worldCoords.getX();
                    Double worldCoordsLat = worldCoords.getY();
                    longitudeEntry.setText(String.valueOf(worldCoordsLong));
                    latitudeEntry.setText(String.valueOf(worldCoordsLat));

                    System.out.println("X: " + mouseX + " Y: " + mouseY);
                    MapMouseListener that = this;
                    SwingUtilities.invokeLater(() -> {
                        mapFrame.getMapPane().removeMouseListener(that);
                        setEnabled(true);
                        getLongLatFromMapButton.setText(GET_LONG_LAT_FROM_MAP_BUTTON_INITIAL_TEXT);

                        // Bring the AddPoint GUI back to the front
                        toFront();
                        repaint();
                    });
                }
                public void onMouseClicked(MapMouseEvent e) {

                }
                public void onMouseReleased(MapMouseEvent e) {

                }
                public void onMouseExited(MapMouseEvent e) {

                }
                public void onMouseEntered(MapMouseEvent e) {

                }
                public void onMouseMoved(MapMouseEvent e) {

                }
                public void onMouseWheelMoved(MapMouseEvent e) {

                }
                public void onMouseDragged(MapMouseEvent e) {

                }
            };
            mapFrame.getMapPane().addMouseListener(mouseListener);

            this.setEnabled(false);
        });

        AddPoint that = this;
        colorPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                System.out.println("Colour picker clicked");
                ColorPicker colorPicker = new ColorPicker(colorState);
                colorPicker.addColorChangeListener(colorPanel);
                colorPicker.addColorChangeListener(that);
                colorPicker.setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        // End setting up action listeners

        // Begin layout of GUI components
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weighty = 0.2;
        panel.add(new JLabel("Longitude:"), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.weighty = 0.2;
        panel.add(longitudeEntry, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weighty = 0.2;
        panel.add(new JLabel("Latitude:"), gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.weighty = 0.2;
        panel.add(latitudeEntry, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.weighty = 0.2;
        gc.gridwidth = 2;
        panel.add(getLongLatFromMapButton, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.weighty = 0.2;
        gc.gridwidth = 1;
        panel.add(new JLabel("Size:"), gc);

        gc.gridx = 1;
        gc.gridy = 3;
        gc.weighty = 0.2;
        panel.add(radiusEntry, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.weighty = 0.2;
        panel.add(new JLabel("Colour:"), gc);

        gc.gridx = 1;
        gc.gridy = 4;
        gc.weighty = 0.2;
        panel.add(colorPanel, gc);

//        gc.gridx = 1;
//        gc.gridy = 5;
//        gc.weighty = 0.2;
//        panel.add(pickColorButton, gc);

        setTitle("Add new point");
        pack();
        // End layout of GUI components

        longitudeEntry.setText(String.valueOf(0));
        latitudeEntry.setText(String.valueOf(0));
//
    }

    private static Point2D.Double screenToWorld(Point2D.Double point, MapViewport mapViewport) {
        AffineTransform screenToWorldTransform = mapViewport.getScreenToWorld();
        Point2D.Double result = new Point2D.Double();
        screenToWorldTransform.transform(point, result);
        return result;
    }

    @Override
    protected LayerComponent createLayerComponent() {
//        event.getLayer().addComponent(createPointComponent(), event);
        return createPointComponent();
    }


    private PointComponent createPointComponent() {
        return new PointComponent(Double.valueOf(longitudeEntry.getText()), Double.valueOf(latitudeEntry.getText()), Double.valueOf((int)radiusEntry.getValue()), colorState);
    }

    public void colorChanged(Color color) {
        colorState = color;
        colorPanel.repaint();
    }
    public static void main(String[] args) {
//        new AddPoint().setVisible(true);
    }
}
