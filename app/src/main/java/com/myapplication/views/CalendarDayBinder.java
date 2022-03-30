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

public class CalendarDayBinder implements DayBinder<DayViewContainer> {

    private final Calendar calendar;
    private final CalendarListener listener;
    private Day mLastSelectedDay;
    private Context context;

    public CalendarDayBinder(Calendar calendar, CalendarListener listener) {
        this.calendar = calendar;
        this.listener = listener;
    }

    public void setSelectedDay(Day day) {
        this.mLastSelectedDay = day;
        listener.onDayClicked(day);
    }

    public void resetSelectedDay() {
        if (this.mLastSelectedDay != null) {
            this.mLastSelectedDay.setIsSelected(false);
        }
        this.mLastSelectedDay = null;
    }

    @NonNull
    @Override
    public DayViewContainer create(@NonNull View view) {
        this.context = view.getContext();
        return new DayViewContainer(view);
    }

    @Override
    public void bind(@NonNull DayViewContainer container, @NonNull CalendarDay calendarDay) {
        String dayText = String.valueOf(calendarDay.getDay());
        container.day.setText(dayText);

        boolean isDayVisible = calendarDay.getOwner() == DayOwner.THIS_MONTH;
        Day day = getCurrentDay(calendarDay);

        applyDynamicFont(container, day, isDayVisible);
        applyBackground(container, day, isDayVisible);
        applyDayDot(container, day, isDayVisible);

        container.day.setOnClickListener(view -> {
            if (!isDayVisible) return;

            if (mLastSelectedDay != null) {
                mLastSelectedDay.setIsSelected(!mLastSelectedDay.isSelected());
            }

            day.setIsSelected(!day.isSelected());
            listener.onDayClicked(day);
            mLastSelectedDay = day;
        });
    }

    private void applyDynamicFont(DayViewContainer container, Day day, boolean isDayVisible) {
        int color;

        if (!isDayVisible) {
            color = ContextCompat.getColor(context, R.color.text_secondary);
        } else if (day.isSelected() || day.isHoliday() || day.isWeekend()) {
            color = ContextCompat.getColor(context, R.color.primary);
        } else {
            color = ContextCompat.getColor(context, R.color.text);
        }
        container.day.setTextColor(color);

        boolean isDayTextBold = day.isSelected() || day.isHoliday() || day.isWeekend();
        if (isDayTextBold && isDayVisible) {
            container.day.setTypeface(null, Typeface.BOLD);
        } else {
            container.day.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void applyBackground(DayViewContainer container, Day day, boolean isDayVisible) {
        if (day.isSelected() && isDayVisible) {
            Drawable bg = ContextCompat.getDrawable(context, R.drawable.calendar_selected_day);
            container.day.setBackground(bg);
        } else {
            container.day.setBackground(null);
        }
    }

    protected void applyDayDot(DayViewContainer container, Day day, boolean isDayVisible) {
        if (day.isHoliday() && isDayVisible) {
            container.imageDot.setVisibility(View.VISIBLE);
            int color = ContextCompat.getColor(context, R.color.holiday);
            container.imageDot.setImageTintList(ColorStateList.valueOf(color));
        } else if (day.hasRecord() && isDayVisible) {
            container.imageDot.setVisibility(View.VISIBLE);
            int color = ContextCompat.getColor(context, R.color.primary);
            container.imageDot.setImageTintList(ColorStateList.valueOf(color));
        } else {
            container.imageDot.setVisibility(View.GONE);
        }
    }

    private Day getCurrentDay(CalendarDay calendarDay) {
        int monthIndex = calendarDay.getDate().getMonth().getValue() - 1;
        Month month = calendar.getMonth(monthIndex);
        int dayIndex = calendarDay.getDay() - 1;
        return month.getDay(dayIndex);
    }

}
