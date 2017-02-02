package org.timetravellersmap.gui.eventpane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joshua on 02/02/17.
 */
public class NameAndDescriptionInput extends JPanel{
    private JTextField nameField = new JTextField();
    private JTextArea descriptionArea = new JTextArea();
    private JCheckBox toggle = new JCheckBox("Name and description?", false);
    private JPanel subPanel = new JPanel();
    private boolean subPanelVisible = false;
    private JFrame ancestorFrame;

    public NameAndDescriptionInput(JFrame ancestorFrame) {
        this.ancestorFrame = ancestorFrame;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
        add(toggle);
        subPanel.add(new JLabel("Name:"));
        subPanel.add(nameField);
        subPanel.add(new JLabel("Description:"));
        subPanel.add(descriptionArea);
        subPanel.setVisible(subPanelVisible);
        add(subPanel);

        toggle.addActionListener(actionEvent -> {
            subPanelVisible = !subPanelVisible;
            subPanel.setVisible(subPanelVisible);
            ancestorFrame.pack();
        });

    }

    public NameAndDescriptionInput() {
        this(null);
    }

    public String getName() {
    return subPanelVisible ? nameField.getText() : null;
}

    public String getDescription() {
        return subPanelVisible ? descriptionArea.getText() : null;
    }

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(new NameAndDescriptionInput());
        window.setSize(200,200);
        window.setVisible(true);
    }
}
