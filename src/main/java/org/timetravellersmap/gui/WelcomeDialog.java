package org.timetravellersmap.gui;

import org.geotools.map.MapContent;
import org.timetravellersmap.TimeTravellersMapException;
import org.timetravellersmap.gui.eventpane.SettingsDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * Created by joshua on 25/03/17.
 */
public class WelcomeDialog extends JFrame {
    private static final String WELCOME_TEXT = "Welcome to Time Traveller's Map";
    private JPanel panel = new JPanel();

    private JButton createNewButton;
    private JButton loadExistingButton;
    private JButton settingsButton;

    private ImageIcon imageIcon;

    private SettingsState settingsState = new SettingsState("Native");

    public WelcomeDialog() {
        java.net.URL imgURL = getClass().getResource("/icon.png");
        if (imgURL != null ) {
            imageIcon = new ImageIcon(imgURL, "image");
        }

//        imageIcon = new ImageIcon("icon.gif");

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints gc = new GridBagConstraints();
        layout.setConstraints(panel, gc);

        setTitle("Time Traveller's Map");

        gc.ipady = 20;
        gc.ipadx = 40;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.insets = new Insets(5, 10, 5, 10);

        JLabel welcomeText = new JLabel(WELCOME_TEXT);
        welcomeText.setFont(new Font("Sans", Font.BOLD, 32));
        panel.add(welcomeText, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;

        JLabel iconLabel = new JLabel(imageIcon);
        iconLabel.setIcon(imageIcon);
//        iconLabel.setPreferredSize(new Dimension(200, 200));
        panel.add(iconLabel, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        createNewButton = new JButton("New...");
        gc.anchor = GridBagConstraints.WEST;
        panel.add(createNewButton, gc);


        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridwidth = 1;
        JLabel createNewDescription = new JLabel("Start a new TTM project");
        createNewDescription.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(createNewDescription, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 1;
        loadExistingButton = new JButton("Load...");
        gc.anchor = GridBagConstraints.WEST;
        panel.add(loadExistingButton, gc);

        gc.gridx = 1;
        gc.gridy = 3;
        gc.gridwidth = 1;
        JLabel loadExistingDescription = new JLabel("Load existing TTM project");
        loadExistingDescription.setFont(new Font("Serif", Font.PLAIN, 20));
//        gc.anchor = GridBagConstraints.EAST;
        panel.add(loadExistingDescription, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 1;
        settingsButton = new JButton("Settings...");
        gc.anchor = GridBagConstraints.WEST;
        panel.add(settingsButton, gc);

        gc.gridx = 1;
        gc.gridy = 4;
        gc.gridwidth = 1;
        JLabel settingsDescription = new JLabel("Configure TTM");
        settingsDescription.setFont(new Font("Serif", Font.PLAIN, 20));
//        gc.anchor = GridBagConstraints.EAST;
        panel.add(settingsDescription, gc);

        Dimension butSize = new Dimension(100, 50);
        createNewButton.setPreferredSize(butSize);
        loadExistingButton.setPreferredSize(butSize);
        settingsButton.setPreferredSize(butSize);

        add(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Begin adding action listeners
        createNewButton.addActionListener(actionEvent -> {
            System.out.println("TODO: spawn new map frame instance");
            new WelcomeWizard().setVisible(true);
            dispose();
        });

        loadExistingButton.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TTM project files", "json");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                System.out.println("TODO: spawn map frame instance for " + fileChooser.getSelectedFile().getPath());
                MapContent mapContent = new MapContent();
                try {
                    MapFrame mf = new MapFrame(mapContent);
                    mf.setJsonFileName(fileChooser.getSelectedFile().getPath());
                    mf.showMap();
                    dispose();
                }
                catch (TimeTravellersMapException e) {
                    e.printStackTrace();
                }
            }
        });

        settingsButton.addActionListener(actionEvent -> {
            setEnabled(false);
            new SettingsDialog(this, settingsState).setVisible(true);
        });

        // End adding action listeners
    }

    public void configure(SettingsState settingsState) {
        System.out.println(settingsState);
        if (settingsState.getStyle().equals("Native")) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
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
        }
        else {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
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
        }
        this.settingsState = settingsState;
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new WelcomeDialog().setVisible(true);
    }
}
