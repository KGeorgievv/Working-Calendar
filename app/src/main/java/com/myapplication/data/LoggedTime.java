package com.myapplication.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "logged_time")
public class LoggedTime {

    public LoggedTime(@NonNull LocalDate date) {
        this.date = date;
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "date")
    private LocalDate date;

    @ColumnInfo(name = "work_hours")
    private int work;

    @ColumnInfo(name = "time_off_hours")
    private int timeOff;

    @ColumnInfo(name = "time_off_unpaid_hours")
    private int unpaidTimeOff;

    @ColumnInfo(name = "sick_time_off_hours")
    private int sickLeave;

    @ColumnInfo(name = "overtime_hours")
    private int overtime;

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

    public boolean hasData() {
        return this.work != 0 ||
                this.timeOff != 0 ||
                this.unpaidTimeOff != 0 ||
                this.overtime != 0 ||
                this.sickLeave != 0;
    }

}
