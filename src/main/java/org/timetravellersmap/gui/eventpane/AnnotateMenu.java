package org.timetravellersmap.gui.eventpane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by joshua on 31/01/17.
 */
public class AnnotateMenu extends JPopupMenu {
    private JMenuItem newPointerAction = new JMenuItem("Pointer");
    private JMenuItem newRectangleAction = new JMenuItem("Rectangle");
    public AnnotateMenu() {
        newPointerAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("TODO: create new pointer");
            }
        });
        newRectangleAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("TODO: create new rectangle");
            }
        });

        this.add(newPointerAction);
        this.add(newRectangleAction);
    }
}
