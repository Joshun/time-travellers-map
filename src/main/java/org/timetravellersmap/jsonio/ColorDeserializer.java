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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * ColorDeserializer: class to add support for deserializing AWT Color objects in gson
 */
public class ColorDeserializer implements JsonDeserializer<Color> {
    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String string = json.getAsJsonPrimitive().getAsString();
        String[] values = string.split(",");
        int red = Integer.valueOf(values[0]);
        int blue = Integer.valueOf(values[1]);
        int green = Integer.valueOf(values[2]);
        int alpha = Integer.valueOf(values[3]);
        return new Color(red, blue, green, alpha);
    }
}
