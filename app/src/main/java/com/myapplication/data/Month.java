package com.myapplication.data;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Обект който съдържа индекса на месеца и неговите дни
 */
public class Month {

    private final int index;
    private final List<Day> days;

    public Month(int index, List<Day> days) {
        this.index = index;
        this.days = days;
    }

    // връща индекса на месеца
    public int getIndex() {
        return this.index;
    }

    // връща името на месеца
    public String getName() {
        String title = java.time.Month.of(index).getDisplayName(TextStyle.FULL, Locale.getDefault());
        return StringUtils.capitalize(title);
    }

    // връща даден ден по индекс
    public Day getDay(int index) {
        return this.days.get(index);
    }

    // връща годината
    public int getYear() {
        return days.get(0).getDay().getYear();
    }

    // връща броя на работните дни
    public int getWorkingDaysCount() {
        return (int) days.stream().filter(day -> !day.isHoliday() && !day.isWeekend()).count();
    }

    // връща броя на дните които са празници или уикенди
    public int getHolidayDaysCount() {
        return (int) days.stream().filter(day -> day.isHoliday() || day.isWeekend()).count();
    }

    // връща всички дати от дните на месеца
    public List<LocalDate> getDays() {
        ArrayList<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            dates.add(days.get(i).getDay());
        }
        return dates;
    }

}
