package com.myapplication.data;

import java.time.LocalDate;

public class Day {

    private final LocalDate day;
    private final boolean isHoliday;
    private boolean isSelected;

    public Day(LocalDate day, boolean isHoliday) {
        this.day = day;
        this.isHoliday = isHoliday;
    }

    public LocalDate getDay() {
        return day;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public boolean isSelected(){
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

}
