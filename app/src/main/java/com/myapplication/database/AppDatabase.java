package com.myapplication.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.myapplication.data.LoggedTime;

/**
 * Локална база данни
 */
@Database(entities = {LoggedTime.class}, version = 1)
@TypeConverters({LocalDateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract LoggedTimeDao loggedTimeDao();

}
