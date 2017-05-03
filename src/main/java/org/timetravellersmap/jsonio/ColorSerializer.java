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
