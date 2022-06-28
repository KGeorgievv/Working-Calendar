package com.myapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.myapplication.data.LoggedTime;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface LoggedTimeDao {

    @Query("SELECT * FROM logged_time WHERE date == :date")
    LoggedTime getLoggedTimeByDate(LocalDate date);

    @Query("SELECT * FROM logged_time WHERE date == :date")
    LiveData<LoggedTime> getLoggedTimeByDateLiveData(LocalDate date);

    @Query("SELECT * FROM logged_time WHERE date IN (:dates)")
    LiveData<List<LoggedTime>> getLoggedTimeForDates(List<LocalDate> dates);

    @Query("SELECT SUM(time_off_hours) as total FROM logged_time")
    int getLoggedTimeOffHours();

    @Insert
    void saveLoggedTime(LoggedTime loggedTime);

    @Query("UPDATE logged_time SET work_hours = :time WHERE date = :date")
    void updateWorkTime(LocalDate date, int time);

    @Query("UPDATE logged_time SET overtime_hours = :time WHERE date = :date")
    void updateOvertimeHours(LocalDate date, int time);

    @Query("UPDATE logged_time SET sick_time_off_hours = :time WHERE date = :date")
    void updateSickTimeOffHours(LocalDate date, int time);

    @Query("UPDATE logged_time SET time_off_hours = :time WHERE date = :date")
    void updateTimeOffHours(LocalDate date, int time);

    @Query("UPDATE logged_time SET time_off_unpaid_hours = :time WHERE date = :date")
    void updateUnpaidTimeOffHours(LocalDate date, int time);



    @Query("DELETE FROM logged_time")
    void clear();



    @Query("UPDATE logged_time SET work_hours = 0 WHERE date = :date")
    void clearWorkTime(LocalDate date);

    @Query("UPDATE logged_time SET overtime_hours = 0 WHERE date = :date")
    void clearOvertimeHours(LocalDate date);

    @Query("UPDATE logged_time SET sick_time_off_hours = 0 WHERE date = :date")
    void clearSickTimeOffHours(LocalDate date);

    @Query("UPDATE logged_time SET time_off_hours = 0 WHERE date = :date")
    void clearTimeOffHours(LocalDate date);

    @Query("UPDATE logged_time SET time_off_unpaid_hours = 0 WHERE date = :date")
    void clearUnpaidTimeOffHours(LocalDate date);

}
