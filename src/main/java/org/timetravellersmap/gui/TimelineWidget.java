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

    public TimelineWidget(double startYear, double endYear) {
        timeline = new Timeline(startYear, endYear, 1, 10);
        this.paintArea = new JPanel(new GridLayout(0, 1)) {
            @Override
            public void paintComponent(Graphics g) {
                paintTimeline((Graphics2D)g, timeline);
            }
        };
        paintArea.setMinimumSize(new Dimension(600, 50));
        setLayout(new GridLayout(0, 1));
        this.add(paintArea);
//        this.setMinimumSize(new Dimension(1000, 1000));
    }

    private static void paintTimeline(Graphics2D graphics2D, Timeline timeline) {
        int screenXCursor = 10;

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
            screenXCursor += 10;
        }
    }
}
