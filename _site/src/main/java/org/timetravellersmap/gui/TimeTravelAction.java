/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.gui;

import org.geotools.swing.action.MapAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * TimeTravelAction: button action for jumping to a specific year
 */
public class TimeTravelAction extends MapAction {
    private MapFrame mapFrame;

    public TimeTravelAction(MapFrame mapFrame) {
        super.init(mapFrame.getMapPane(), "Time travel", "Jump to a year", "/timetravel.png");
        this.mapFrame = mapFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String inputValue = JOptionPane.showInputDialog(mapFrame, "Enter a year", "Time Travel", JOptionPane.PLAIN_MESSAGE);
        // If user cancels dialog, return
        if (inputValue == null) {
            return;
        }
        // Otherwise, try to parse integer and seek to the year
        try {
            int year = Integer.parseInt(inputValue);
            if (year == 0) {
                throw new NumberFormatException("Year zero does not exist");
            }
            System.out.println("time travel: " + year);
            System.out.println(mapFrame);
            System.out.println(mapFrame.getTimelineWidget());
            mapFrame.getTimelineWidget().setPointerJump(year);
            mapFrame.getTimelineWidget().redraw();
            mapFrame.timelineChanged(year, true);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mapFrame, "Please enter a valid year and try again", "Invalid Year", JOptionPane.ERROR_MESSAGE);
        }
    }
}
