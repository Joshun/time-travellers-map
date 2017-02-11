package org.timetravellersmap.gui.annotatepane;

import org.timetravellersmap.gui.eventpane.AddPoint;

import javax.swing.*;

/**
 * Created by joshua on 31/01/17.
 */
public class AnnotateMenu extends JPopupMenu {
    private JMenuItem newPointerAction = new JMenuItem("Point");
    private JMenuItem newRectangleAction = new JMenuItem("Rectangle");

    private boolean visible = false;

//    private MapFrame mapFrame;
    private AnnotatePane annotatePane;

    public AnnotateMenu(AnnotatePane parentAnnotatePane) {
        this.annotatePane = parentAnnotatePane;
        newPointerAction.addActionListener(actionEvent ->  {
            new AddPoint(annotatePane, annotatePane.getSelectedEvent()).setVisible(true);
        });
        newRectangleAction.addActionListener(actionEvent -> {
            System.out.println("TODO: create new rectangle");
        });

        this.add(newPointerAction);
        this.add(newRectangleAction);
    }


}