package org.timetravellersmap.gui;

import net.miginfocom.layout.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

/**
 * Created by joshua on 26/01/17.
 */
public class TimelineWidget extends JPanel {
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

    public TimelineWidget(double startYear, double endYear, int width, int height) {
        this.start = startYear;
        this.end = endYear;
        this.minorInterval = 1;
        this.majorInterval = 10;
        this.width = width;
        this.height = height;
//        timeline = new Timeline(startYear, endYear, 1, 10);
        setTimeline(start, end, minorInterval, majorInterval);
        this.paintArea = new JPanel(new GridLayout(0, 1)) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintTimeline((Graphics2D)g, timeline, width, height, pointerPosition);
            }
        };
        paintArea.setMinimumSize(new Dimension(width, height));
        paintArea.setPreferredSize(new Dimension(width, height));
//        setLayout(new GridLayout(0, 3));
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        System.out.println("width " + getWidth());
//        this.add(paintArea);

        nextHundredYearsButton = new JButton("100 >");
        prevHundredYearsButton = new JButton("< 100");
        prevThousandYearsButton = new JButton("< 1000");
        nextThousandYearsButton = new JButton("1000 >");

        nextHundredYearsButton.addActionListener(makeSeekButtonListener(100));
        prevHundredYearsButton.addActionListener(makeSeekButtonListener(-100));
        nextThousandYearsButton.addActionListener(makeSeekButtonListener(1000));
        prevThousandYearsButton.addActionListener(makeSeekButtonListener(-1000));

//        nextHundredYearsButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                start += 100;
//                end += 100;
//                setTimeline(start, end, minorInterval, majorInterval);
//                setPointer(pointerPosition+100);
//                paintArea.repaint();
//            }
//        });
//
//        prevHundredYearsButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                start -= 100;
//                end -= 100;
//                setTimeline(start, end, minorInterval, majorInterval);
//                setPointer(pointerPosition-100);
//                paintArea.repaint();
//            }
//        });



//        this.setMinimumSize(new Dimension(1000, 1000));

        Graphics graphics = paintArea.getGraphics();
//        System.out.println(graphics);

        paintArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                updatePointer(mouseEvent.getX());
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

        paintArea.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                updatePointer(mouseEvent.getX());
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        });

        prevHundredYearsButton.setMaximumSize(new Dimension(100, height/2));
        nextHundredYearsButton.setMaximumSize(new Dimension(100, height/2));
        prevThousandYearsButton.setMaximumSize(new Dimension(100, height/2));
        nextThousandYearsButton.setMaximumSize(new Dimension(100, height/2));

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
        gridBagConstraints.fill = GridBagConstraints.NONE;
        this.add(prevYearsContainer, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(paintArea, gridBagConstraints);

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        this.add(nextYearsContainer, gridBagConstraints);
        paintArea.repaint();
    }

    private void setTimeline(double start, double end, double minorInterval, double majorInterval) {
        this.timeline = new Timeline(start, end, minorInterval, majorInterval);
    }

    private void updatePointer(int xPos) {
        System.out.println("end" + end + " xpos " + xPos);
        // Ignore out-of-range clicks
        if (xPos < width) {
            System.out.println("click " + xPos);
            int year = computeYearClicked(xPos, 0, width, start, end);
            System.out.println("year " + year);
            setPointer(year);
            paintArea.repaint();
        }
    }

    private ActionListener makeSeekButtonListener(int yearDifference) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                start += yearDifference;
                end += yearDifference;
                setTimeline(start, end, minorInterval, majorInterval);
                setPointer(pointerPosition+yearDifference);
                paintArea.repaint();
            }
        };
    }

    private void forceRepaint() {
        Graphics g = getGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());
        repaint();
    }

    public void setPointer(double timePosition) {
        this.pointerPosition = timePosition;
    }

    private static int computeYearClicked(double xMousePosition, double xDrawOffset, double barWidth, double start, double end) {
        // Using linear interpolation, compute the year in the timeline nearest to the mouse click

        // Difference between end and start years
        double yearWidth = end - start;

        // Fraction of timeline bar's screen area that was clicked
        double proportionOfBar = (xMousePosition-xDrawOffset)/barWidth;

        // start year + (proportion of bar area * bar width)
        // this returns the nearest year to the click
        return (int)Math.round(start + (proportionOfBar * yearWidth));
    }

    private static void paintTimeline(Graphics2D graphics2D, Timeline timeline, int width, int height, double pointerPosition) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Font font = new Font("Serif", Font.PLAIN, 10);
        graphics2D.setFont(font);

        int timelineSize = (int) (timeline.getEnd() - timeline.getStart());
        double count = timelineSize / timeline.getMinorInterval();
        double increment = width / count;

        int screenXCursor = 0;
        int textYOffset = 10;
        int lineYOffset = 20;

//        graphics2D.setStroke(new BasicStroke(100));

        System.out.println("paint");

        for (TimelineCursor timelineCursor: timeline) {
            double timePosition = timelineCursor.getPosition();
            System.out.println("draw " + timePosition);
            boolean isMajorInterval = timelineCursor.isMajorInterval();

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
            screenXCursor += increment;
        }
    }
}
