package org.timetravellersmap.core;

import com.j256.ormlite.table.DatabaseTable;

/**
 * Descriptor: representation of name and description for various objects
 */
@DatabaseTable(tableName = "descriptors")
public class Descriptor {
    private final String name;
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
