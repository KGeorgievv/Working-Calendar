package com.myapplication.data;

import androidx.annotation.StringRes;

import com.myapplication.R;

public enum TimeType {

    Work(R.string.work_time),

    TimeOff(R.string.time_off),

    UnpaidTimeOff(R.string.time_off_unpaid),

    SickTimeOff(R.string.sick_leave),

    Overtime(R.string.overtime);

    private final int strRes;

    TimeType(@StringRes int strRes) {
        this.strRes = strRes;
    }

    public int getStrRes() {
        return strRes;
    }

}
