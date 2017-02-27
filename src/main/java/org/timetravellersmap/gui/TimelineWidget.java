package org.timetravellersmap.gui;

import org.timetravellersmap.core.event.EventChangeListener;
import org.timetravellersmap.core.event.EventIndex;
import org.timetravellersmap.core.timeline.Timeline;
import org.timetravellersmap.core.timeline.TimelineChangeListener;
import org.timetravellersmap.core.timeline.TimelineCursor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * TimelineWidget: a widget for selecting the current year / date
 * Allows selecting within a century and skipping 100 and 1000 year periods
 */
public class TimelineWidget extends JPanel implements EventChangeListener {
    private JPanel paintArea;
    private JButton prevHundredYearsButton;
    private JButton nextHundredYearsButton;
    private JButton prevThousandYearsButton;
    private JButton nextThousandYearsButton;

    private Timeline timeline;
    private double pointerPosition;
    private int width;
    private int height;

    // start and end years
    private double start;
    private double end;
    private double minorInterval;
    private double majorInterval;

    private MapFrame mapFrame;

    private ArrayList<TimelineChangeListener> changeListeners = new ArrayList<>();

    public TimelineWidget(double startYear, double endYear, int width, int height, MapFrame parentMapFrame) {
        this.start = startYear;
        this.end = endYear;
        this.minorInterval = 1;
        this.majorInterval = 10;
        this.width = width;
        this.height = height;
        this.mapFrame = parentMapFrame;
        setTimeline(start, end, minorInterval, majorInterval);

        // Setup the paint area, i.e. where the core itself is drawn
        this.paintArea = new JPanel(new GridLayout(0, 1)) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintTimeline((Graphics2D)g, timeline, mapFrame, width, height, pointerPosition);
            }
        };
        paintArea.setMinimumSize(new Dimension(width, height));
        paintArea.setPreferredSize(new Dimension(width, height));
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        System.out.println("width " + getWidth());

        nextHundredYearsButton = new JButton(">");
        nextHundredYearsButton.setToolTipText("Forward 100 years");
        prevHundredYearsButton = new JButton("<");
        prevHundredYearsButton.setToolTipText("Back 100 years");
        nextThousandYearsButton = new JButton(">>");
        nextThousandYearsButton.setToolTipText("Forward 1000 years");
        prevThousandYearsButton = new JButton("<<");
        prevThousandYearsButton.setToolTipText("Back 1000 years");

        // Bind listeners to prev / next year buttons
        nextHundredYearsButton.addActionListener(makeSeekButtonListener(100));
        prevHundredYearsButton.addActionListener(makeSeekButtonListener(-100));
        nextThousandYearsButton.addActionListener(makeSeekButtonListener(1000));
        prevThousandYearsButton.addActionListener(makeSeekButtonListener(-1000));


        // Register mouse click events on core and change pointer accordingly
        paintArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                // Update the selected year pointer when the user clicks ruler
                updatePointer(mouseEvent.getX());
                fireChangeListeners(getPointerYear(), true);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                updatePointer(mouseEvent.getX());
                fireChangeListeners(getPointerYear(), true);
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        });

        // Register mouse drag events on core and change pointer accordingly
        paintArea.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                // Update the current year pointer when the user drags slider
                updatePointer(mouseEvent.getX());
                // fire event change listeners when dragged
                // if UPDATE_MAP_ON_DRAG is True, redraw the map while dragging, else only when released
                fireChangeListeners(getPointerYear(), MapFrame.UPDATE_MAP_ON_DRAG);
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                int year = computeYearClicked(mouseEvent.getX(), 0, width, start, end);
                String eventSummary = mapFrame.getEventIndex().generateStartEventSummary(year);
                if (eventSummary != null) {
                    paintArea.setToolTipText(eventSummary);

                    System.out.println(eventSummary);
                }
                else {
                    paintArea.setToolTipText("");
                }
            }
        });

        // Begin layout of GUI components
        prevHundredYearsButton.setMaximumSize(new Dimension(75, height/2));
        nextHundredYearsButton.setMaximumSize(new Dimension(75, height/2));
        prevThousandYearsButton.setMaximumSize(new Dimension(75, height/2));
        nextThousandYearsButton.setMaximumSize(new Dimension(75, height/2));

        JPanel prevYearsContainer = new JPanel();
        prevYearsContainer.setLayout(new BoxLayout(prevYearsContainer, BoxLayout.PAGE_AXIS));
        prevYearsContainer.add(prevHundredYearsButton);
        prevYearsContainer.add(prevThousandYearsButton);

        JPanel nextYearsContainer = new JPanel();
        nextYearsContainer.setLayout(new BoxLayout(nextYearsContainer, BoxLayout.PAGE_AXIS));
        nextYearsContainer.add(nextHundredYearsButton);
        nextYearsContainer.add(nextThousandYearsButton);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.ipadx = 0;
        this.add(prevYearsContainer, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.ipadx = 25;
        this.add(paintArea, gridBagConstraints);

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.ipadx = 0;
        this.add(nextYearsContainer, gridBagConstraints);
        // End layout of GUI components

        paintArea.repaint();

    }

    // Add a listener for TimelineChange events
    public void addChangeListener(TimelineChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    // Remove a listener for TimelineChange events
    public void removeChangeListener(TimelineChangeListener changeListener) {
        changeListeners.remove(changeListener);
    }

    // Notify TimelineChange listeners that the timeline has changed (i.e. user click / seek)
    private void fireChangeListeners(int year, boolean redraw) {
        for (TimelineChangeListener changeListener: changeListeners) {
            changeListener.timelineChanged(year, redraw);
        }
    }

    private void setTimeline(double start, double end, double minorInterval, double majorInterval) {
        this.timeline = new Timeline(start, end, minorInterval, majorInterval);
    }

    private void updatePointer(int xPos) {
        System.out.println("end" + end + " xpos " + xPos);
        // Ignore out-of-range clicks
        if (xPos < width && xPos >= 0) {
            System.out.println("click " + xPos);
            int year = computeYearClicked(xPos, 0, width, start, end);
            System.out.println("year " + year);
            setPointer(year);
//            fireChangeListeners(year);
            paintArea.repaint();
        }
    }

    public void redraw() {
        repaint();
    }

    private ActionListener makeSeekButtonListener(int yearDifference) {
        return actionEvent -> {
                start += yearDifference;
                end += yearDifference;
                setTimeline(start, end, minorInterval, majorInterval);
                setPointer(pointerPosition+yearDifference);
                paintArea.repaint();
        };
    }

    public void setPointer(double timePosition) {
        this.pointerPosition = timePosition;
        System.out.println("position " + timePosition);
    }

    private static int computeYearClicked(double xMousePosition, double xDrawOffset, double barWidth, double start, double end) {
        // Using linear interpolation, compute the year in the core nearest to the mouse click

        // Difference between end and start years
        double yearWidth = end - start;

        // Fraction of core bar's screen area that was clicked
        double proportionOfBar = (xMousePosition-xDrawOffset)/barWidth;

        // start year + (proportion of bar area * bar width)
        // this returns the nearest year to the click
        return (int)Math.round(start + (proportionOfBar * yearWidth));
    }

    public void eventChanged() {
        redraw();
    }

    private static void paintTimeline(Graphics2D graphics2D, Timeline timeline, MapFrame mapFrame, int width, int height, double pointerPosition) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Serif", Font.PLAIN, 8);
        graphics2D.setFont(font);

        int timelineSize = (int) (timeline.getEnd() - timeline.getStart());
        double count = timelineSize / timeline.getMinorInterval();
        double increment = width / count;

        int screenXCursor = 0;
        int textYOffset = 10;
        int lineYOffset = 20;

        EventIndex eventIndex = mapFrame.getEventIndex();

        System.out.println("paint");

        for (TimelineCursor timelineCursor: timeline) {
            double timePosition = timelineCursor.getPosition();
            boolean isMajorInterval = timelineCursor.isMajorInterval();
            
            // Drawing of the timeline ruler lines
            if (isMajorInterval) {
                graphics2D.setPaint(new Color(0, 0, 0));
                graphics2D.drawString(String.valueOf((int)timePosition), screenXCursor, textYOffset);
            }

            if (timePosition == pointerPosition) {
                graphics2D.setStroke(new BasicStroke(4));
                graphics2D.setPaint(new Color(255, 0, 0));

                graphics2D.draw(new Line2D.Double(screenXCursor, lineYOffset, screenXCursor, lineYOffset + 15));
            }
            else {
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setPaint(new Color(0, 0, 0));

                if (isMajorInterval) {
                    graphics2D.draw(new Line2D.Double(screenXCursor, lineYOffset, screenXCursor, lineYOffset + 10));
                } else {
                    graphics2D.draw(new Line2D.Double(screenXCursor, lineYOffset, screenXCursor, lineYOffset + 5));
                }
            }
           
            
            // Drawing of the counts for event start and end
            int eventStartCount = eventIndex.countStartEventsForYear((int)timePosition);
            int eventEndCount = eventIndex.countEndEventsForYear((int)timePosition);
            drawEventCountText(graphics2D, screenXCursor, lineYOffset+20, eventStartCount);
            drawEventCountText(graphics2D, screenXCursor, lineYOffset+28, eventEndCount);
            
            // Increment the x cursor position by increment value
            screenXCursor += increment;
        }
    }
    
    private static void drawEventCountText(Graphics2D graphics2D, int screenXCursor, int screenYCursor, int countValue) {
        if (countValue > 0) {
            graphics2D.setPaint(new Color(0, 0, 0));
            String countString;
            if (countValue > 9) {
                countString = "+";
            }
            else {
                countString = String.valueOf(countValue);
            }
            graphics2D.drawString(countString, screenXCursor, screenYCursor);
        }
    }
    
    public int getPointerYear() {
        return (int)pointerPosition;
    }
}
