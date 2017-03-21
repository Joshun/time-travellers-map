package org.timetravellersmap.jsonio;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.awt.*;
import java.lang.reflect.Type;

/**
 * Created by joshua on 21/03/17.
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
