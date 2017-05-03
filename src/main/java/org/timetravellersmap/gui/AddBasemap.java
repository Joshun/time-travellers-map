package org.timetravellersmap.gui;

import org.geotools.map.FeatureLayer;
import org.opengis.filter.expression.Add;
import org.timetravellersmap.basemapio.BasemapLoaderFactory;
import org.timetravellersmap.core.Basemap;
import org.timetravellersmap.gui.BasemapManager;
import org.timetravellersmap.gui.MapFrame;
import org.timetravellersmap.gui.eventpane.AddModifyEventDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * AddBasemap: dialog for adding basemap files along with their associated valid years
 */
public class AddBasemap extends JFrame {
    private MapFrame mapFrame;
    private BasemapManager basemapManager;
    private JPanel panel = new JPanel();
    private JTextField fileNameField = new JTextField(50);
    private JButton fileBrowseButton = new JButton("Browse...");
    private JTextField nameField = new JTextField(50);
    private JSpinner startDateEntry = new JSpinner();
    private JSpinner endDateEntry;

    private JButton addBasemapButton = new JButton("Add basemap");
    private JButton cancelButton = new JButton("Cancel");

    public AddBasemap(MapFrame ancestorMapFrame, BasemapManager parentBasemapManager) {
        this.mapFrame = ancestorMapFrame;
        this.basemapManager = parentBasemapManager;
        fileNameField.setEditable(false);
        startDateEntry = new JSpinner(new SpinnerNumberModel(1900, -4000, 4000, 1));
        startDateEntry.setEditor(new JSpinner.NumberEditor(startDateEntry, "#"));
        endDateEntry = new JSpinner(new SpinnerNumberModel(2000, -4000, 4000, 1));
        endDateEntry.setEditor(new JSpinner.NumberEditor(endDateEntry, "#"));

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        layout.setConstraints(panel, gc);
        panel.setLayout(layout);

        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Name:"), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        panel.add(nameField, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        panel.add(new JLabel("Filename:"), gc);

        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        panel.add(fileNameField, gc);

        gc.gridx = 2;
        gc.gridy = 1;
        gc.gridwidth = 1;
        panel.add(fileBrowseButton, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        panel.add(new JLabel("Valid start year:"), gc);

        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridwidth = 2;
        panel.add(startDateEntry, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 1;
        panel.add(new JLabel("Valid end year:"), gc);

        gc.gridx = 1;
        gc.gridy = 3;
        gc.gridwidth = 2;
        panel.add(endDateEntry, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 1;
        panel.add(addBasemapButton, gc);

        gc.gridx = 1;
        gc.gridy = 4;
        gc.gridwidth = 2;
        panel.add(cancelButton, gc);

        add(panel);
        pack();
        setTitle("Add Basemap");

        // Begin adding action listeners
        fileBrowseButton.addActionListener(actionEvent -> {
            JFileChooser chooser = new JFileChooser();
            boolean allowExperimentalFileFormats = ancestorMapFrame.getSettingsState().getUseExperimentalFileFormats();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Map files (" + BasemapLoaderFactory.getFilebrowserDescription(allowExperimentalFileFormats) + ")",
                    BasemapLoaderFactory.getSupportedFormats(allowExperimentalFileFormats)
                );
            chooser.setFileFilter(filter);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                fileNameField.setText(chooser.getSelectedFile().getPath());
            }
        });

        addBasemapButton.addActionListener(actionEvent -> {
//            Calendar startYear = AddModifyEventDialog.yearToCalendar((int) startDateEntry.getValue());
//            Calendar endYear = AddModifyEventDialog.yearToCalendar((int) endDateEntry.getValue());
            int startYear = (int)startDateEntry.getValue();
            int endYear = (int)endDateEntry.getValue();
            Basemap basemap = new Basemap(fileNameField.getText(),
                nameField.getText(),
                startYear,
                endYear
            );
            mapFrame.getBasemapList().addBasemap(basemap);
            basemapManager.basemapsChanged();
            dispose();
        });

        cancelButton.addActionListener(actionEvent -> {
            dispose();
        });
        // End adding action listeners
    }

//    public static void main(String[] args) {
//        org.timetravellersmap.gui.AddBasemap ab = new org.timetravellersmap.gui.AddBasemap();
//        ab.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ab.setVisible(true);
//    }
}
