package com.myapplication.data;

import org.apache.commons.lang3.StringUtils;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Month {

    private final int index;
    private final List<Day> days;

    public Month(int index, List<Day> days) {
        this.index = index;
        this.days = days;
    }

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        String title = java.time.Month.of(index).getDisplayName(TextStyle.FULL, Locale.getDefault());
        return StringUtils.capitalize(title);
    }

    public Day getDay(int index) {
        return this.days.get(index);
    }

    public int getYear() {
        return days.get(0).getDay().getYear();
    }

    public int getWorkingDaysCount() {
        return (int) days.stream().filter(day -> !day.isHoliday() && !day.isWeekend()).count();
    }

    public int getHolidayDaysCount() {
        return (int) days.stream().filter(day -> day.isHoliday() || day.isWeekend()).count();
    }

}
