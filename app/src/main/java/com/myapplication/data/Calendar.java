package com.myapplication.data;

import java.util.List;

/**
 * Обект който съдържа текущата година и месеците в нея
 */
public class Calendar {

    // годината
    private final Integer year;
    // лист от месеците
    private final List<Month> months;

    public Calendar(Integer year, List<Month> months) {
        this.year = year;
        this.months = months;
    }

    // връща лист с месеците
    public List<Month> getMonths() {
        return this.months;
    }

    // връща даден месец по индекс
    public Month getMonth(int index) {
        return this.months.get(index);
    }

}
