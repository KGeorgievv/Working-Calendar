package com.myapplication.views;

import android.view.View;

import androidx.annotation.NonNull;

import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;

public class MonthHeaderBinder implements MonthHeaderFooterBinder<MonthViewContainer> {

    @Override
    public void bind(@NonNull MonthViewContainer viewContainer, @NonNull CalendarMonth calendarMonth) {
    }

    // фунцкия която се извиква когато се създава header-а на календара
    @NonNull
    @Override
    public MonthViewContainer create(@NonNull View view) {
        return new MonthViewContainer(view);
    }

}
