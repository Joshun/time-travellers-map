package org.timetravellersmap.gui;

/**
 * Created by joshua on 03/04/17.
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
