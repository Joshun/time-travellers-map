package org.timetravellersmap.jsonio;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * GregorianCalendarSerializer: class to properly serialize GregorianCalendar
 * This properly serializes BC, unlike the built-in deserializer
 */
public class GregorianCalendarSerializer implements JsonSerializer<GregorianCalendar> {

    @Override
    public JsonElement serialize(final GregorianCalendar calendar, final Type typeOfSrc, final JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("year", calendar.get(Calendar.YEAR));
        jsonObject.addProperty("month", calendar.get(Calendar.MONTH));
        jsonObject.addProperty("day", calendar.get(Calendar.DAY_OF_MONTH));
        jsonObject.addProperty("hour", calendar.get(Calendar.HOUR));
        jsonObject.addProperty("minute", calendar.get(Calendar.MINUTE));
        jsonObject.addProperty("second", calendar.get(Calendar.SECOND));
        // Save ERA field, required to store BC events properly
        // Store as String since this is more portable than internal Java constants
        jsonObject.addProperty("era",
            calendar.get(Calendar.ERA) == GregorianCalendar.BC ? "bc" : "ad"
        );
        return jsonObject;
    }
}
