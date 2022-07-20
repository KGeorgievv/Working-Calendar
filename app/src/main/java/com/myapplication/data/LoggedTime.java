package com.myapplication.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

/**
 * Обект който съдържа данни с записаните часове
 */
@Entity(tableName = "logged_time")
public class LoggedTime {

    public LoggedTime(@NonNull LocalDate date) {
        this.date = date;
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "date")
    private LocalDate date; // дата

    @ColumnInfo(name = "work_hours")
    private int work; // работни часове

    @ColumnInfo(name = "time_off_hours")
    private int timeOff; // почивни часове

    @ColumnInfo(name = "time_off_unpaid_hours")
    private int unpaidTimeOff; // часове за неплатен отпуск

    @ColumnInfo(name = "sick_time_off_hours")
    private int sickLeave; // часове за болнични

    @ColumnInfo(name = "overtime_hours")
    private int overtime; // часове за извънреден труд

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    public void setDate(@NonNull LocalDate date) {
        this.date = date;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public int getTimeOff() {
        return timeOff;
    }

    public void setTimeOff(int timeOff) {
        this.timeOff = timeOff;
    }

    public int getUnpaidTimeOff() {
        return unpaidTimeOff;
    }

    public void setUnpaidTimeOff(int unpaidTimeOff) {
        this.unpaidTimeOff = unpaidTimeOff;
    }

    public int getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(int sickLeave) {
        this.sickLeave = sickLeave;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    // връща дали записани данни или не
    public boolean hasData() {
        return this.work != 0 ||
                this.timeOff != 0 ||
                this.unpaidTimeOff != 0 ||
                this.overtime != 0 ||
                this.sickLeave != 0;
    }

}
