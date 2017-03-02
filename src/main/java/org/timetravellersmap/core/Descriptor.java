package org.timetravellersmap.core;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Descriptor: representation of name and description for various objects
 */
@DatabaseTable(tableName = "descriptors")
public class Descriptor {
    @DatabaseField
    private final String name;
    @DatabaseField
    private final String description;

    @DatabaseField(generatedId = true)
    private int id;

    public Descriptor(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Descriptor() {
        this(null, null);
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
