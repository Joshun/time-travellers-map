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
     * Launches the application
     */
    public static void main(String[] args) throws Exception {
        WelcomeDialog welcomeDialog = new WelcomeDialog();
        welcomeDialog.setVisible(true);
    }
}
