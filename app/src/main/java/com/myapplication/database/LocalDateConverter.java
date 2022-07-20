package com.myapplication.database;

import androidx.room.TypeConverter;

import java.time.LocalDate;

/**
 * Обект който съдържа логика как дадени обекти се записват в базата
 */
public class LocalDateConverter {

    // обръща записана дата в базата в такава която се използва в приложението
    @TypeConverter
    public LocalDate storedStringToLocalDate(String value) {
        return LocalDate.parse(value);
    }

    // обръща дата която се ползва в приложението в такава която се пази в базата
    @TypeConverter
    public String localDateToStoredString(LocalDate date) {
        return date.toString();
    }

}
