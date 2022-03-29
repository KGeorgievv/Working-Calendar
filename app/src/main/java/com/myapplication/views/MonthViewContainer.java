package com.myapplication.views;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kizitonwose.calendarview.ui.ViewContainer;
import com.myapplication.R;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class MonthViewContainer extends ViewContainer {

    public MonthViewContainer(@NonNull View view) {
        super(view);

        LinearLayout container = view.findViewById(R.id.container);

        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        days.add(DayOfWeek.FRIDAY);
        days.add(DayOfWeek.SATURDAY);
        days.add(DayOfWeek.SUNDAY);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0F
        );

        for (int index = 0; index < days.size(); index++) {
            DayOfWeek day = days.get(index);
            String displayName = day.getDisplayName(TextStyle.NARROW, Locale.getDefault());

            TextView textView = new TextView(getView().getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            textView.setText(displayName);
            container.addView(textView);
        }
    }

}
