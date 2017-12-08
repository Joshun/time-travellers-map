/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap;

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
        // Try to set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Show WelcomeDialog
        WelcomeDialog welcomeDialog = new WelcomeDialog();
        welcomeDialog.setVisible(true);
    }
}
