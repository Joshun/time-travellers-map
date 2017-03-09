package org.timetravellersmap.core;

import com.google.gson.annotations.Expose;

/**
 * Descriptor: representation of name and description for various objects
 */
public class Descriptor {
    @Expose
    private final String name;
    @Expose
    private final String description;

    public Descriptor(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Descriptor(String name) {
        this(name, null);
    }

    public String toString() {
        return "Descriptor name: " + name + " description: " + description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
