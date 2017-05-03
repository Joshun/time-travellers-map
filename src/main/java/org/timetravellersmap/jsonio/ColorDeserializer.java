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
