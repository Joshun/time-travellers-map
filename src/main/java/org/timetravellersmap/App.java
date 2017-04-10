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
