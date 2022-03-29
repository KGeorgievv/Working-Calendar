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

    public Integer getYear() {
        return days.get(0).getDay().getYear();
    }

    public Integer getWorkingDaysCount() {
        return (int) days.stream().filter(day -> !day.isHoliday()).count();
    }

    public Integer getHolidayDaysCount() {
        return (int) days.stream().filter(Day::isHoliday).count();
    }

}
