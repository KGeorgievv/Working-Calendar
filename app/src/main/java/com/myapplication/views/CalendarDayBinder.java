package com.myapplication.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.myapplication.R;
import com.myapplication.data.Calendar;
import com.myapplication.data.Day;
import com.myapplication.data.Month;

/**
 * Обект който съдържа логика за това как даден ден се показва в календара
 */
public class CalendarDayBinder implements DayBinder<DayViewContainer> {

    private final Calendar calendar;
    private final CalendarListener listener;
    private Day mLastSelectedDay;
    private Context context;

    public CalendarDayBinder(Calendar calendar, CalendarListener listener) {
        this.calendar = calendar;
        this.listener = listener;
    }

    // запазва кой е селектираният ден
    public void setSelectedDay(Day day) {
        this.mLastSelectedDay = day;
        listener.onDayClicked(day);
    }

    // фунцкия която се извиква когато се създава изгледа на деня от месеца
    @NonNull
    @Override
    public DayViewContainer create(@NonNull View view) {
        this.context = view.getContext();
        return new DayViewContainer(view);
    }

    // фунцкия която се извиква за всеки един ден от текущият месец
    @Override
    public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay calendarDay) {
        String dayText = String.valueOf(calendarDay.getDay());

        // задава числото на деня
        container.day.setText(dayText);

        // проверява дали деня е в текущият месец
        boolean isDayVisible = calendarDay.getOwner() == DayOwner.THIS_MONTH;
        // взима деня който се използва от приложението
        Day day = getCurrentDay(calendarDay);

        // прилага промените по шрифта
        applyDynamicFont(container, day, isDayVisible);
        // прилага промениет по фона
        applyBackground(container, day, isDayVisible);
        // прилага промените за точката
        applyDayDot(container, day, isDayVisible);

        // функция която се извиква, когато даден ден е натиснат
        container.day.setOnClickListener(view -> {
            // ако деня не е в текъщият месец нищо не става
            if (!isDayVisible) return;

            // проверява дали има предишен селектиран ден
            // и го зачиства, ако има такъв
            if (mLastSelectedDay != null) {
                mLastSelectedDay.setIsSelected(false);
            }

            // запазва кой ден е селектиран
            day.setIsSelected(!day.isSelected());
            listener.onDayClicked(day);
            mLastSelectedDay = day;
        });
    }

    // фунцкия която променя шрифта на даден ден от месеца
    private void applyDynamicFont(DayViewContainer container, Day day, boolean isDayVisible) {
        int color;

        // ако денят не е в текущият месец
        // цвета шрифта се променя на сив
        if (!isDayVisible) {
            color = ContextCompat.getColor(context, R.color.text_secondary);

        // ако деня е селектиран или е почивен или е уикенд
        // цвета на шрифта се променя на син
        } else if (day.isSelected() || day.isHoliday() || day.isWeekend()) {
            color = ContextCompat.getColor(context, R.color.primary);

        // за всички останали случаи шрифта се променя на черен
        } else {
            color = ContextCompat.getColor(context, R.color.text);
        }
        container.day.setTextColor(color);

        // проверява дали шрифта трябва да стане удебелен
        // удебелен е когато деня е селектиран или е почивен или е уикенд
        boolean isDayTextBold = day.isSelected() || day.isHoliday() || day.isWeekend();

        // променя шрифта спрямо това дали трябва да е удебелен или не
        if (isDayTextBold && isDayVisible) {
            container.day.setTypeface(null, Typeface.BOLD);
        } else {
            container.day.setTypeface(null, Typeface.NORMAL);
        }
    }

    // функция която слага синьо кръгче на текущият селектиран ден
    private void applyBackground(DayViewContainer container, Day day, boolean isDayVisible) {
        if (day.isSelected() && isDayVisible) {
            Drawable bg = ContextCompat.getDrawable(context, R.drawable.calendar_selected_day);
            container.day.setBackground(bg);
        } else {
            container.day.setBackground(null);
        }
    }

    // функция която променя цветната точка на деня
    protected void applyDayDot(DayViewContainer container, Day day, boolean isDayVisible) {
        // ако деня е почивен и е в текущият месец
        // променя цветната точка на оранжева
        if (day.isHoliday() && isDayVisible) {
            container.imageDot.setVisibility(View.VISIBLE);
            int color = ContextCompat.getColor(context, R.color.holiday);
            container.imageDot.setImageTintList(ColorStateList.valueOf(color));

        // ако деня има записани часове и е в текущият месец
        // променя цветната точка на оранжева
        } else if (day.hasRecord() && isDayVisible) {
            container.imageDot.setVisibility(View.VISIBLE);
            int color = ContextCompat.getColor(context, R.color.primary);
            container.imageDot.setImageTintList(ColorStateList.valueOf(color));

        // ако нито едно от двете горе не е вярно точката се маха
        } else {
            container.imageDot.setVisibility(View.GONE);
        }
    }

    // връща текущият избран ден
    private Day getCurrentDay(CalendarDay calendarDay) {
        int monthIndex = calendarDay.getDate().getMonth().getValue() - 1;
        Month month = calendar.getMonth(monthIndex);
        int dayIndex = calendarDay.getDay() - 1;
        return month.getDay(dayIndex);
    }

}
