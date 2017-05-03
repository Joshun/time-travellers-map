package org.timetravellersmap.gui;

import org.timetravellersmap.core.Basemap;
import org.timetravellersmap.core.BasemapList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * BasemapManager: a GUI for adding and removing basemaps, and displaying those loaded,
 *  along with their name and period of validity
 */
public class BasemapManager extends JFrame {
    private JTable basemapTable;
    private BasemapList basemapList;
    private String[] columnNames = {"Name", "Path", "Valid Start", "Valid End"};

    private JButton addBasemapButton = new JButton("Add...");
    private JButton removeBasemapButton = new JButton("Remove");

    private JPanel panel = new JPanel();

    public BasemapManager(MapFrame ancestorMapFrame, BasemapList basemapList) {
        this.basemapList = basemapList;
        basemapTable = new JTable();
        // Populate table
        basemapsChanged();

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints gc = new GridBagConstraints();
        layout.setConstraints(panel, gc);

        gc.gridwidth = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(addBasemapButton, gc);

        gc.gridwidth = 1;
        gc.gridx = 1;
        gc.gridy = 0;
        panel.add(removeBasemapButton, gc);

        gc.gridwidth = 2;
        gc.gridx = 0;
        gc.gridy = 1;

        JScrollPane basemapTableContainer = new JScrollPane(basemapTable);
        basemapTableContainer.setMinimumSize(new Dimension(500, 500));
        basemapTable.setFillsViewportHeight(true);
        panel.add(basemapTableContainer, gc);

        add(panel);
        pack();
        setTitle("Manage Basemaps");

        // Begin add action listeners
        addBasemapButton.addActionListener(actionEvent -> {
            new AddBasemap(ancestorMapFrame, this).setVisible(true);
        });

        removeBasemapButton.addActionListener(actionEvent -> {
            ArrayList<Basemap> flattened = basemapList.getFlattenedBasemaps();
            int selectedRow = basemapTable.getSelectedRow();
            if (selectedRow >= 0) {
                basemapList.removeBasemap(flattened.get(selectedRow));
                basemapsChanged();
            }
        });
        // End add action listeners
    }

    public void basemapsChanged() {
        basemapTable.setModel(new DefaultTableModel(
                BasemapList.generateTableRows(basemapList.getFlattenedBasemaps()),
                columnNames));
    }

    public JPanel getPanel() {
        return panel;
    }

    public static void main(String[] args) {
        MapFrame mapFrame = null;
        BasemapList bsl = new BasemapList();
        bsl.addBasemap(new Basemap("filename", "mapname", 1900, 1901));
        new BasemapManager(mapFrame, bsl).setVisible(true);
    }
}
