package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.timeline.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 01/02/17.
 */
public class AnnotatePane extends JPanel {
    private AnnotateMenu annotateMenu = new AnnotateMenu();
    private JButton addAnnotationButton = new JButton("Add annotation...");

    private MapFrame mapFrame;

    private boolean visible = false;

    public AnnotatePane(MapFrame parentMapFrame) {
        this.mapFrame = parentMapFrame;
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        addAnnotationButton.addActionListener(actionEvent ->  {
            System.out.println("Annotate");
            org.timetravellersmap.timeline.Event event = mapFrame.getEventPane().getSelectedEvent();
            if (event != null) {
                JButton btn = (JButton) actionEvent.getSource();
                // Spawn popup annotate menu underneath button
                int x = btn.getX();
                int y = btn.getY() + btn.getHeight();
                annotateMenu.show(this, x, y);
            }
        });


        gc.gridx = 0;
        gc.gridy = 0;
        this.add(addAnnotationButton, gc);
    }

    public boolean toggleVisibleState() {
        visible = !visible;
        System.out.println("AnnotatePane.visible="+visible);
        setVisible(visible);
        return visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
