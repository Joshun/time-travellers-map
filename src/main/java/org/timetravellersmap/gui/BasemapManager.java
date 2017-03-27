package org.timetravellersmap.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Created by joshua on 27/03/17.
 */
public class BasemapManager extends JFrame {
    private JTable basemapTable;

    public BasemapManager() {
        basemapTable = new JTable(new DefaultTableModel());
    }
}
