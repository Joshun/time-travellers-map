package org.timetravellersmap.gui;

import org.timetravellersmap.core.Basemap;
import org.timetravellersmap.core.BasemapList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Date;

/**
 * Created by joshua on 27/03/17.
 */
public class BasemapManager extends JFrame {
    private JTable basemapTable;
    private DefaultTableModel basemapTableModel;
    private BasemapList basemapList;
    private String[] columnNames = {"Name", "Valid Start", "Valid End"};

    private JButton addBasemapButton = new JButton("Add...");
    private JButton removeBasemapButton = new JButton("Remove");

    private JPanel panel = new JPanel();

    public BasemapManager(MapFrame ancestorMapFrame, BasemapList basemapList) {
        this.basemapList = basemapList;
        basemapTable = new JTable();
        basemapTableModel = new DefaultTableModel();
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
        basemapTable.setFillsViewportHeight(true);
        panel.add(basemapTableContainer, gc);

        add(panel);
        pack();
        setTitle("Manage Basemaps");
    }

    public void basemapsChanged() {
        System.out.println(basemapList.generateTableRows().length);
//        basemapTableModel.setDataVector(basemapList.generateTableRows(), columnNames);
        basemapTable.setModel(new DefaultTableModel(basemapList.generateTableRows(), columnNames));
    }

    public static void main(String[] args) {
        MapFrame mapFrame = null;
        BasemapList bsl = new BasemapList();
        bsl.addBasemap(new Basemap("filename", "mapname", new Date(), new Date()));
        new BasemapManager(mapFrame, bsl).setVisible(true);
    }
}
