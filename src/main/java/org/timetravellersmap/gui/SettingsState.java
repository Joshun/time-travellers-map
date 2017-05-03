package org.timetravellersmap.gui;

/**
 * SettingsState: class to represent current global settings state of program
 */
public class SettingsState {
    private String style;
    private boolean useExperimentalFileFormats;

    public SettingsState(String style, boolean useExperimentalFileFormats) {
        this.style = style;
        this.useExperimentalFileFormats = useExperimentalFileFormats;
    }

    public SettingsState() {
        this("Native", false);
    }

    public String getStyle() {
        return style;
    }

    public boolean getUseExperimentalFileFormats() {
        return useExperimentalFileFormats;
    }
}
