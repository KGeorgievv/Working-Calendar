package com.myapplication.data;

import androidx.annotation.StringRes;

import com.myapplication.R;

/**
 * Енумерация която съдържа различните типове часове
 */
public enum TimeType {

    // работни часове
    Work(R.string.work_time),

    // почивни часове
    TimeOff(R.string.time_off),

    // неплатени почивни дни
    UnpaidTimeOff(R.string.time_off_unpaid),

    // болнични часове
    SickTimeOff(R.string.sick_leave),

    // извънредни часове
    Overtime(R.string.overtime);

    private final int strRes;

    TimeType(@StringRes int strRes) {
        this.strRes = strRes;
    }

    public int getStrRes() {
        return strRes;
    }

}
