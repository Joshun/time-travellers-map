package org.timetravellersmap.gui;

import org.geotools.map.MapContent;
import org.timetravellersmap.TimeTravellersMapException;
import org.timetravellersmap.core.BasemapList;
import org.timetravellersmap.gui.eventpane.LayerManager;
import org.timetravellersmap.overlay.LayerList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by joshua on 05/04/17.
 */
public class WelcomeWizard extends JFrame {
    private ArrayList<WizardItem> wizardItems = new ArrayList<>();
    private int currentWizard = 0;
    private JPanel container = new JPanel();
    private JButton prevButton = new JButton("Previous");
    private JButton nextButton = new JButton("Next");

    private BasemapList basemapList = new BasemapList();
    private MapContent mapContent = new MapContent();
    private LayerList layerList = new LayerList(mapContent, null);
    private MapFrame mapFrame;

    private class WizardItem {
        private JPanel panel;
        private String title;
        private String instruction;

        private WizardItem(JPanel panel, String title, String instruction) {
            this.panel = panel;
            this.title = title;
            this.instruction = instruction;
        }

        public JPanel getPanel() {
            return panel;
        }

        public String getText() {
            return "<html><body><h1>" + title + "</h1><br><h2>" + instruction + "</h2></body></html>";
        }
    }

    private void nextWizard() {
        System.out.println("next");
        if (currentWizard+1 < wizardItems.size()) {
            currentWizard++;
            WizardItem currentItem = wizardItems.get(currentWizard);
            makeUI(currentItem);
        }
        else {
            if (launch()) {
                mapFrame.setEnabled(true);
                mapFrame.setTitle("Time Traveller's Map");
                dispose();
            }
            else {
                System.out.println("Save cancelled");
            }
        }
    }

    private void prevWizard() {
        System.out.println("prev");
        if (currentWizard-1 >= 0) {
            currentWizard--;
            WizardItem currentItem = wizardItems.get(currentWizard);
            makeUI(currentItem);
        }
    }

    private void createWizards() {
        try {
            mapFrame = new MapFrame(mapContent);
        }
        catch (TimeTravellersMapException e) {

        }
        BasemapManager basemapManager = new BasemapManager(mapFrame,  basemapList);
        LayerManager layerManager = new LayerManager(layerList, mapFrame);
        JPanel nameDescription = new JPanel();
        nameDescription.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        nameDescription.add(new JLabel("Name:"), gc);
        gc.gridx = 1;
        gc.gridy = 0;
        nameDescription.add(new JTextField(20), gc);
        gc.gridx = 0;
        gc.gridy = 1;
        nameDescription.add(new JLabel("Description:"), gc);
        gc.gridx = 1;
        gc.gridy = 1;
        nameDescription.add(new JTextArea(3, 20), gc);

        wizardItems.add(new WizardItem(
                nameDescription,
                "New Project",
                "Enter name and description"
        ));

        wizardItems.add(new WizardItem(
            basemapManager.getPanel(),
            "Configure Basemaps",
                "Select the maps to use"
        ));

        wizardItems.add(new WizardItem(
                layerManager.getPanel(),
                "Configure Layers",
                "Select the layers to use"
        ));
    }

    private void makeUI(WizardItem wizardItem) {
        setTitle("New TTM Project");
        remove(container);
        container = new JPanel();
        container.setLayout(new BorderLayout(10, 50));
        JPanel currentWizardPanel = wizardItem.getPanel();
        String currentWizardText = wizardItem.getText();

        container.add(new JLabel(currentWizardText), BorderLayout.PAGE_START);
        container.add(currentWizardPanel, BorderLayout.CENTER);

        JPanel buttonContainer = new JPanel(new GridLayout(1, 2));
        buttonContainer.add(prevButton);
        buttonContainer.add(nextButton);
        container.add(buttonContainer, BorderLayout.PAGE_END);

        add(container);
        pack();
    }

    private File savePrompt() {
        JFileChooser fileChooser = new JFileChooser("Save project");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TTM Project Files", "json");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        else {
            return null;
        }
    }

    private boolean launch() {
        File fileToSave = savePrompt();
        if (fileToSave != null) {
            mapFrame.setJsonFileName(fileToSave.getPath());
            mapFrame.showMap();
            return true;
        }
        return false;
    }

    public WelcomeWizard() {
        setTitle("New TTM Project");
        setPreferredSize(new Dimension(1000, 1000));
        createWizards();

        container.setLayout(new BorderLayout(10, 50));
        makeUI(wizardItems.get(0));
        add(container);
        pack();
        nextButton.addActionListener(actionEvent -> nextWizard());
        prevButton.addActionListener(actionEvent -> prevWizard());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                new WelcomeDialog().setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        WelcomeWizard w = new WelcomeWizard();
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.setVisible(true);
    }
}
