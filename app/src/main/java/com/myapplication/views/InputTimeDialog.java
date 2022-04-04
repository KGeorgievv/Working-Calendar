package com.myapplication.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.myapplication.R;
import com.myapplication.Utils;
import com.myapplication.data.TimeType;

public class InputTimeDialog {

    private final Context context;
    private TimeType type;
    private AlertDialog.Builder builder;

    public InputTimeDialog(Context context, TimeType type) {
        this.context = context;
        this.type = type;

        View content = generateContent();

        builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(generateTitle(type));
        builder.setView(content);
        builder.setPositiveButton("Add", (dialogInterface, i) -> {

        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {

        });

        View input = content.findViewById(R.id.input);
        Utils.showKeyBoard(input);
    }

    public void show() {
        builder.show();
    }

    private View generateContent() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.dialog_input_time, null);
    }

    private View generateTitle(TimeType type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewTitle = inflater.inflate(R.layout.dialog_title, null);
        View header = viewTitle.findViewById(R.id.header);
        TextView title = viewTitle.findViewById(R.id.text);
        header.setBackgroundColor(getHeaderColor(type));
        title.setText(context.getString(type.getStrRes()));
        return viewTitle;
    }

    private int getHeaderColor(TimeType type) {
        int headerColor = 0;
        switch (type) {
            case Work:
                headerColor = ContextCompat.getColor(this.context, R.color.work);
                break;
            case TimeOff:
                headerColor = ContextCompat.getColor(this.context, R.color.time_off);
                break;
            case UnpaidTimeOff:
                headerColor = ContextCompat.getColor(this.context, R.color.time_off_unpaid);
                break;
            case SickTimeOff:
                headerColor = ContextCompat.getColor(this.context, R.color.sick_leave);
                break;
            case Overtime:
                headerColor = ContextCompat.getColor(this.context, R.color.overtime);
                break;
        }
        return headerColor;
    }

}
