package org.timetravellersmap;

/**
 * Created by joshua on 23/01/17.
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
}
