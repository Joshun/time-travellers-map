/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

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

    public Descriptor() {
        this("", "");
    }

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

    public static String encodeMultilineHTML(String rawString) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<rawString.length(); i++) {
            char charAt = rawString.charAt(i);
            sb.append(charAt);
            if (charAt == '\n') {
                sb.append("<br>");
            }
        }
        return sb.toString();
    }

    public String getTooltipText() {
        String descriptionEncoded = encodeMultilineHTML(description);

        return "<html>" +
                "<b>" + name + "</b>" + "<br>"
                + descriptionEncoded
                + "</html>";


    }
}
