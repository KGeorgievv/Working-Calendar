package com.myapplication.database;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class LocalDateConverter {

    @TypeConverter
    public LocalDate storedStringToLocalDate(String value) {
        return LocalDate.parse(value);
    }

    @TypeConverter
    public String localDateToStoredString(LocalDate date) {
        return date.toString();
    }

}
