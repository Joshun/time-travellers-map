package org.timetravellersmap.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by joshua on 26/01/17.
 */
public class TimelineWidget extends JPanel {
    private JPanel paintArea;
    private Timeline timeline;
    private int width;
    private int height;

    public TimelineWidget(double startYear, double endYear, int width, int height) {
        this.width = width;
        this.height = height;
        timeline = new Timeline(startYear, endYear, 1, 10);
        this.paintArea = new JPanel(new GridLayout(0, 1)) {
            @Override
            public void paintComponent(Graphics g) {
                paintTimeline((Graphics2D)g, timeline, width, height);
            }
        };
        paintArea.setMinimumSize(new Dimension(600, 50));
        setLayout(new GridLayout(0, 1));
        System.out.println("width " + getWidth());
        this.add(paintArea);
//        this.setMinimumSize(new Dimension(1000, 1000));
    }

    private static void paintTimeline(Graphics2D graphics2D, Timeline timeline, int width, int height) {
        int timelineSize = (int) (timeline.getEnd() - timeline.getStart());
        double count = timelineSize / timeline.getMinorInterval();
        double increment = width / count;

        int screenXCursor = 0;

//        graphics2D.setStroke(new BasicStroke(100));

        System.out.println("paint");

        for (TimelineCursor timelineCursor: timeline) {
            double timePosition = timelineCursor.getPosition();
            System.out.println("draw " + timePosition);
            boolean isMajorInterval = timelineCursor.isMajorInterval();
            if (isMajorInterval) {
                graphics2D.draw(new Line2D.Double(screenXCursor, 10, screenXCursor, 50));
            }
            else {
                graphics2D.draw(new Line2D.Double(screenXCursor, 10, screenXCursor, 25));
            }
            screenXCursor += increment;
        }
    }
}
