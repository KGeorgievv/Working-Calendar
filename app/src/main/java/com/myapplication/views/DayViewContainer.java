package com.myapplication.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kizitonwose.calendarview.ui.ViewContainer;
import com.myapplication.R;

public class DayViewContainer extends ViewContainer {

    public View root;
    public TextView day;
    public ImageView imageDot;

    public DayViewContainer(@NonNull View view) {
        super(view);

        this.root = view.findViewById(R.id.root);
        this.day = view.findViewById(R.id.text_day);
        this.imageDot = view.findViewById(R.id.image_dot);
    }

}
