/* Copyright (c) 2017, Joshua O'Leary and The University of Sheffield
 * All rights reserved.
 *
 * This software was built as part of the individual project “A Time Traveller’s Map” at the
 * Computer Science Department, The University of Sheffield. The idea for this project was
 * provided by the project supervisor, Dr Siobhán North.
 *
 * This software is LICENSED under a BSD 3-Clause licence, see LICENCE for full details.
 */

package org.timetravellersmap.jsonio;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * ColorSerializer: class to add support for serializing AWT Color objects in gson
 */
public class ColorSerializer implements JsonSerializer<Color> {
    public JsonElement serialize(Color color, Type typeOfColor, JsonSerializationContext context) {
        String jsonString = String.valueOf(color.getRed())
                + "," + String.valueOf(color.getGreen())
                + "," + String.valueOf(color.getBlue())
                + "," + String.valueOf(color.getAlpha());
        return new JsonPrimitive(jsonString);
    }
}
