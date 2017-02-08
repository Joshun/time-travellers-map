package org.timetravellersmap.core;

/**
 * Annotation: representation of name and description for various objects
 */
public class Annotation {
    private final String name;
    private final String description;

    public Annotation(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Annotation(String name) {
        this(name, null);
    }

    public String toString() {
        return "Annotation name: " + name + " description: " + description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
