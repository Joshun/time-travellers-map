package org.timetravellersmap.gui.eventpane;

import org.timetravellersmap.gui.SettingsState;
import org.timetravellersmap.gui.WelcomeDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by joshua on 03/04/17.
 */
public class SettingsDialog extends JFrame {
    private JPanel panel = new JPanel();
    private WelcomeDialog welcomeDialog;
    private JComboBox<String> styleChooser = new JComboBox<>();
    private static final String[] styleChoices = { "Swing", "Native"};

    public SettingsDialog(WelcomeDialog parentWelcomeDialog) {
        this.welcomeDialog = parentWelcomeDialog;
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        panel.setLayout(layout);
        styleChooser.setModel(new DefaultComboBoxModel<>(styleChoices));

        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Style:"), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        panel.add(styleChooser, gc);

        add(panel);
        pack();
        setTitle("Settings");

        // Start adding action listeners
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parentWelcomeDialog.setEnabled(true);
            }
        });

        styleChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                welcomeDialog.configure(new SettingsState((String)styleChooser.getSelectedItem()));
            }
        });
        // End adding action listeners
    }
}
