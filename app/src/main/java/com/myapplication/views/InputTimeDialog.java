package com.myapplication.views;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.myapplication.R;
import com.myapplication.Utils;
import com.myapplication.config.PreferenceManager;
import com.myapplication.data.LoggedTime;
import com.myapplication.data.TimeType;
import com.myapplication.data.User;
import com.myapplication.database.LoggedTimeDao;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

public class InputTimeDialog {

    private final Context context;
    private final AlertDialog.Builder builder;
    private final View content;
    private final LocalDate date;
    private final TextInputEditText viewInput;

    public InputTimeDialog(
            Context context,
            LocalDate date,
            TimeType type,
            LoggedTimeDao loggedTimeDao
    ) {
        this.context = context;
        this.date = date;

        User user = new PreferenceManager(context).getUser();
        content = generateContent();

        viewInput = content.findViewById(R.id.input);

        builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(generateTitle(type));
        builder.setView(content);
        builder.setPositiveButton("Add", (dialogInterface, i) -> {
            logHours(type, loggedTimeDao);
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });

        prefillHours(type, loggedTimeDao);
        setupTitle(type);
        setupHintText(type, user, loggedTimeDao);

        Utils.showKeyBoard(viewInput);
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

    private void setupTitle(TimeType type) {
        TextView viewTitle = content.findViewById(R.id.text_title);
        String typeStr = context.getString(type.getStrRes());
        String title = context.getString(R.string.input_hours, typeStr);
        viewTitle.setText(title);
    }

    private void setupHintText(TimeType type, User user, LoggedTimeDao dao) {
        TextView viewHint = content.findViewById(R.id.text_hint);
        TextInputEditText viewInput = content.findViewById(R.id.input);

        if (type == TimeType.TimeOff) {
            viewHint.setVisibility(View.VISIBLE);
            int daysLeft = user.getPtoDays() - dao.getLoggedTimeOffHours();

            if (daysLeft > 0) {
                String text = context.getString(
                        R.string.hint_remaining_days,
                        daysLeft,
                        user.getPtoDays()
                );
                viewHint.setText(text);
            } else {
                viewHint.setText(R.string.no_days_left);
                viewInput.setEnabled(false);
            }
        } else {
            viewHint.setVisibility(View.GONE);
        }
    }

    private void prefillHours(TimeType type, LoggedTimeDao dao) {
        LoggedTime loggedTime = dao.getLoggedTimeByDate(date);
        if (loggedTime == null) return;

        int hours = 0;
        switch (type) {
            case Work:
                hours = loggedTime.getWork();
                break;
            case TimeOff:
                hours = loggedTime.getTimeOff();
                break;
            case UnpaidTimeOff:
                hours = loggedTime.getUnpaidTimeOff();
                break;
            case SickTimeOff:
                hours = loggedTime.getSickLeave();
                break;
            case Overtime:
                hours = loggedTime.getOvertime();
                break;
        }

        if (hours != 0) {
            viewInput.setText(String.valueOf(hours));
            viewInput.setSelection(viewInput.length());
        }
    }

    private void logHours(TimeType type, LoggedTimeDao dao) {
        String hoursStr = viewInput.getText().toString();
        if (StringUtils.isBlank(hoursStr)) return;

        boolean hasRecord = dao.getLoggedTimeByDate(date) != null;
        if (!hasRecord) dao.saveLoggedTime(new LoggedTime(date));

        int hours = Integer.parseInt(hoursStr);
        switch (type) {
            case Work:
                dao.updateWorkTime(date, hours);
                break;
            case TimeOff:
                dao.updateTimeOffHours(date, hours);
                break;
            case UnpaidTimeOff:
                dao.updateUnpaidTimeOffHours(date, hours);
                break;
            case SickTimeOff:
                dao.updateSickTimeOffHours(date, hours);
                break;
            case Overtime:
                dao.updateOvertimeHours(date, hours);
                break;
        }
    }

}
