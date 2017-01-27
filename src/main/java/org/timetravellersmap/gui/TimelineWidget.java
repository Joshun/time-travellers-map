package org.timetravellersmap.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

/**
 * Created by joshua on 26/01/17.
 */
public class TimelineWidget extends JPanel {
    private JPanel paintArea;
    private Timeline timeline;
    private double pointerPosition;
    private int width;
    private int height;

    public TimelineWidget(double startYear, double endYear, int width, int height) {
        this.width = width;
        this.height = height;
        timeline = new Timeline(startYear, endYear, 1, 10);
        this.paintArea = new JPanel(new GridLayout(0, 1)) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintTimeline((Graphics2D)g, timeline, width, height, pointerPosition);
            }
        };
        paintArea.setMinimumSize(new Dimension(600, 50));
        setLayout(new GridLayout(0, 1));
        System.out.println("width " + getWidth());
        this.add(paintArea);
//        this.setMinimumSize(new Dimension(1000, 1000));

        Graphics graphics = paintArea.getGraphics();
//        System.out.println(graphics);

        this.addMouseListener(new MouseListener() {
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

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                updatePointer(mouseEvent.getX());
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        });

    }

    private void updatePointer(int xPos) {
        // Ignore out-of-range clicks
        if (xPos <= 600) {
            System.out.println("click " + xPos);
            int year = computeYearClicked(xPos, 0, 600, 1900, 2000);
            System.out.println("year " + year);
            setPointer(year);
            repaint();
        }
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
