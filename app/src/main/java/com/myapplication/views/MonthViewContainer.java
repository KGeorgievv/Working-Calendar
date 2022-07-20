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

/**
 * Обект който съдържа логика за това как даден месец се показва в календара
 */
public class MonthViewContainer extends ViewContainer {

    public MonthViewContainer(@NonNull View view) {
        super(view);

        LinearLayout container = view.findViewById(R.id.container);

        // генериране на дните от седмицата които ще се показват
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        days.add(DayOfWeek.FRIDAY);
        days.add(DayOfWeek.SATURDAY);
        days.add(DayOfWeek.SUNDAY);

        // генериране на изгледа
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0F
        );

        // цикъл който обхожда всички дни от седмицата
        for (int index = 0; index < days.size(); index++) {
            DayOfWeek day = days.get(index);

            // генериране на буквата на деня от седмицата
            String displayName = day.getDisplayName(TextStyle.NARROW, Locale.getDefault());

            // генериране на текст
            TextView textView = new TextView(getView().getContext());
            // добавяане не подравняване на текста - центриран
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            // добавяне на буквата на деня
            textView.setText(displayName);
            // добавяне на текста към изгледа
            container.addView(textView);
        }
    }

}
