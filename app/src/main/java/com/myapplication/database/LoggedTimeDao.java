package com.myapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.myapplication.data.LoggedTime;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс който съдържа операциите с базата данни
 */
@Dao
public interface LoggedTimeDao {

    // връща записани часове спрямо подадена дата
    @Query("SELECT * FROM logged_time WHERE date == :date")
    LoggedTime getLoggedTimeByDate(LocalDate date);

    // връща записани часове спрямо подадена дата
    // и се ъпдейтва автоматично, когато се променят данните
    @Query("SELECT * FROM logged_time WHERE date == :date")
    LiveData<LoggedTime> getLoggedTimeByDateLiveData(LocalDate date);

    // връща лист от записани часове спрямо подадена дата
    // и се ъпдейтва автоматично, когато се променят данните
    @Query("SELECT * FROM logged_time WHERE date IN (:dates)")
    LiveData<List<LoggedTime>> getLoggedTimeForDates(List<LocalDate> dates);

    // връща общият брой на записаните часове за отпуска
    @Query("SELECT SUM(time_off_hours) as total FROM logged_time")
    int getLoggedTimeOffHours();

    // запазва обект с записани часове
    @Insert
    void saveLoggedTime(LoggedTime loggedTime);

    // записва работни часове спрямо дадена дата
    @Query("UPDATE logged_time SET work_hours = :time WHERE date = :date")
    void updateWorkTime(LocalDate date, int time);

    // записва извънредни часове спрямо дадена дата
    @Query("UPDATE logged_time SET overtime_hours = :time WHERE date = :date")
    void updateOvertimeHours(LocalDate date, int time);

    // записва болнични часове спрямо дадена дата
    @Query("UPDATE logged_time SET sick_time_off_hours = :time WHERE date = :date")
    void updateSickTimeOffHours(LocalDate date, int time);

    // записва часове за отпуска спрямо дадена дата
    @Query("UPDATE logged_time SET time_off_hours = :time WHERE date = :date")
    void updateTimeOffHours(LocalDate date, int time);

    // записва часове за неплатен отпуск спрямо дадена дата
    @Query("UPDATE logged_time SET time_off_unpaid_hours = :time WHERE date = :date")
    void updateUnpaidTimeOffHours(LocalDate date, int time);

    // изтрива всички записани часове
    @Query("DELETE FROM logged_time")
    void clear();

    // изтрива работните часове спрямо дадена дата
    @Query("UPDATE logged_time SET work_hours = 0 WHERE date = :date")
    void clearWorkTime(LocalDate date);

    // изтрива извнредните часове спрямо дадена дата
    @Query("UPDATE logged_time SET overtime_hours = 0 WHERE date = :date")
    void clearOvertimeHours(LocalDate date);

    // изтрива болничните часове спрямо дадена дата
    @Query("UPDATE logged_time SET sick_time_off_hours = 0 WHERE date = :date")
    void clearSickTimeOffHours(LocalDate date);

    // изтрива часовете за отпуска спрямо дадена дата
    @Query("UPDATE logged_time SET time_off_hours = 0 WHERE date = :date")
    void clearTimeOffHours(LocalDate date);

    // изтрива неплатените работни часове спрямо дадена дата
    @Query("UPDATE logged_time SET time_off_unpaid_hours = 0 WHERE date = :date")
    void clearUnpaidTimeOffHours(LocalDate date);

}
