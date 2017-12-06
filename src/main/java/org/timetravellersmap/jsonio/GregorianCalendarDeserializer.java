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
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * GregorianCalendarDeserializer: class to properly deserialize GregorianCalendar
 * This properly deserializes BC, unlike the built-in deserializer
 */
public class GregorianCalendarDeserializer implements JsonDeserializer<GregorianCalendar> {
    @Override
    public GregorianCalendar deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) {
        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonElement year = jsonObject.get("year");
        final JsonElement month = jsonObject.get("month");
        final JsonElement day = jsonObject.get("day");
        final JsonElement hour = jsonObject.get("hour");
        final JsonElement minute = jsonObject.get("minute");
        final JsonElement second = jsonObject.get("second");
        final JsonElement era = jsonObject.get("era");

        GregorianCalendar calendar = new GregorianCalendar(year.getAsInt(), month.getAsInt(), day.getAsInt(), hour.getAsInt(), minute.getAsInt(), second.getAsInt());
        // Retrieve and set ERA field since this is very important for this application to work properly
        String eraString = era.getAsString();
        calendar.set(Calendar.ERA,
            "bc".equals(eraString) ? GregorianCalendar.BC : GregorianCalendar.AD
        );

        return calendar;
    }
}
