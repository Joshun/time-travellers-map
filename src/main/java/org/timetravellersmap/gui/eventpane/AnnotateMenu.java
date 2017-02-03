package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.timeline.Event;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by joshua on 31/01/17.
 */
public class AnnotateMenu extends JPopupMenu {
    private JMenuItem newPointerAction = new JMenuItem("Point");
    private JMenuItem newRectangleAction = new JMenuItem("Rectangle");

    private boolean visible = false;

    private MapFrame mapFrame;

    public AnnotateMenu(MapFrame ancestorMapFrame) {
        this.mapFrame = ancestorMapFrame;
        newPointerAction.addActionListener(actionEvent ->  {
            new AddPoint(ancestorMapFrame).setVisible(true);
        });
        newRectangleAction.addActionListener(actionEvent -> {
            System.out.println("TODO: create new rectangle");
        });

        this.add(newPointerAction);
        this.add(newRectangleAction);
    }


}
