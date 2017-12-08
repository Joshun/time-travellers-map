/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

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
 * SettingsDialog: a dialog for configuring global program settings
 * Currently includes theme choice, and toggle for experimental file format support
 */
public class SettingsDialog extends JFrame {
    private JPanel panel = new JPanel();
    private WelcomeDialog welcomeDialog;
    private JComboBox<String> styleChooser = new JComboBox<>();
    private JComboBox<String> experimentalFileChoicesChooser = new JComboBox<>();
    private static final String[] styleChoices = { "Native", "Swing" };
    private static final String[] experimentalFileChoices = { "Yes", "No" };

    private JButton okButton = new JButton("OK");
    private JButton cancelButton = new JButton("Cancel");

    public SettingsDialog(WelcomeDialog parentWelcomeDialog, SettingsState currentSettingsState) {
        this.welcomeDialog = parentWelcomeDialog;
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        panel.setLayout(layout);
        styleChooser.setModel(new DefaultComboBoxModel<>(styleChoices));
        experimentalFileChoicesChooser.setModel(new DefaultComboBoxModel<>(experimentalFileChoices));

        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Theme:"), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        panel.add(styleChooser, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(new JLabel("Use experimental file formats?"), gc);

        gc.gridx = 1;
        gc.gridy = 1;
        panel.add(experimentalFileChoicesChooser, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        panel.add(okButton, gc);

        gc.gridx = 1;
        gc.gridy = 2;
        panel.add(cancelButton, gc);

        if (currentSettingsState != null) {
            System.out.println(currentSettingsState.getStyle());
            System.out.println(currentSettingsState.getUseExperimentalFileFormats());
            styleChooser.setSelectedItem(currentSettingsState.getStyle());
            experimentalFileChoicesChooser.setSelectedItem(currentSettingsState.getUseExperimentalFileFormats() ? "Yes" : "No");
        }

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

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println(experimentalFileChoicesChooser.getSelectedItem());
                welcomeDialog.configure(new SettingsState(
                        (String) styleChooser.getSelectedItem(),
                        "Yes".equals((String) experimentalFileChoicesChooser.getSelectedItem())
                ));
                parentWelcomeDialog.setEnabled(true);
                dispose();
            }
        });

        cancelButton.addActionListener(actionEvent -> {
            parentWelcomeDialog.setEnabled(true);
            dispose();
        });

//        styleChooser.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                welcomeDialog.configure(new SettingsState((String)styleChooser.getSelectedItem()));
//            }
//        });
//
//        experimentalFileChoicesChooser.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//
//            }
//        });
        // End adding action listeners
    }
}
