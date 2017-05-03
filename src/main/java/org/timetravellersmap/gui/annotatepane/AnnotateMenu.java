package org.timetravellersmap.gui.annotatepane;

import org.timetravellersmap.gui.MapFrame;

import javax.swing.*;

/**
 * AnnotateMenu: dropdown menu for adding different types of LayerComponent
 */
public class AnnotateMenu extends JPopupMenu {
    private JMenuItem newPointerAction = new JMenuItem("Point");
    private JMenuItem newRectangleAction = new JMenuItem("Rectangle");

    private boolean visible = false;

//    private MapFrame mapFrame;
    private AnnotatePane annotatePane;

    public AnnotateMenu(MapFrame ancestorMapFrame, AnnotatePane parentAnnotatePane) {
        this.annotatePane = parentAnnotatePane;
        newPointerAction.addActionListener(actionEvent ->  {
            new AddPoint(ancestorMapFrame, annotatePane, annotatePane.getSelectedEvent()).setVisible(true);
        });
        newRectangleAction.addActionListener(actionEvent -> {
            new AddRectangle(ancestorMapFrame, annotatePane, annotatePane.getSelectedEvent()).setVisible(true);
        });

        this.add(newPointerAction);
        this.add(newRectangleAction);
    }


}
