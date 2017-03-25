package org.timetravellersmap;

import org.timetravellersmap.gui.Toplevel;
import org.timetravellersmap.gui.WelcomeDialog;

import javax.swing.*;

/**
 * App: the launcher for the program
 *
 */
public class App
{
    /**
     * GeoTools Quickstart demo application. Prompts the user for a shapefile and displays its
     * contents on the screen in a map frame
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Toplevel toplevelGui = new Toplevel();
        toplevelGui.show();
    }
}
