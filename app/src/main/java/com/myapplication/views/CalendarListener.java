package com.myapplication.views;

import com.myapplication.data.Day;

/**
 * Интерфейс който информира кога е кликнат даден ден от календара
 */
public interface CalendarListener {

    void onDayClicked(Day day);

}
