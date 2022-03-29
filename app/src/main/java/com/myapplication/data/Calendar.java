package com.myapplication.data;

import java.time.LocalDate;
import java.util.List;

public class Calendar {

    private final Integer year;
    private final List<Month> months;

    public Calendar(Integer year, List<Month> months) {
        this.year = year;
        this.months = months;
    }

    public Integer getYear() {
        return this.year;
    }

    public List<Month> getMonths() {
        return this.months;
    }

    public Month getMonth(int index) {
        return this.months.get(index);
    }

    public Month getCurrentMonth() {
        LocalDate now = LocalDate.now();
        return this.months.get(now.getMonthValue() - 1);
    }

}
