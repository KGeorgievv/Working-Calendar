package com.myapplication.data;

import java.time.LocalDate;

public class Day {

    private final LocalDate day;
    private final boolean isWeekend;
    private final String holiday;

    private boolean isSelected;
    private boolean hasRecord;

    public Day(LocalDate day, boolean isWeekend, String holiday, boolean hasRecord) {
        this.day = day;
        this.isWeekend = isWeekend;
        this.holiday = holiday;
        this.hasRecord = hasRecord;
    }

    public LocalDate getDay() {
        return this.day;
    }

    public String getHoliday() {
        return holiday;
    }

    public boolean isHoliday() {
        return this.holiday != null && !this.holiday.isEmpty();
    }

    public boolean isWeekend() {
        return this.isWeekend;
    }

    public boolean hasRecord() {
        return hasRecord;
    }

    public void setHasRecord(boolean hasRecord) {
        this.hasRecord = hasRecord;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
