package com.myapplication.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.myapplication.R;
import com.myapplication.data.Day;
import com.myapplication.data.LoggedTime;
import com.myapplication.data.Month;
import com.myapplication.data.TimeType;
import com.myapplication.databinding.FragmentMainBinding;
import com.myapplication.viewmodel.CalendarViewModel;
import com.myapplication.views.CalendarDayBinder;
import com.myapplication.views.CalendarListener;
import com.myapplication.views.InputTimeDialog;
import com.myapplication.views.MonthHeaderBinder;

import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class MainFragment extends BaseFragment implements View.OnClickListener, CalendarListener {

    private FragmentMainBinding binding;
    private CalendarViewModel viewModel;
    private CalendarDayBinder dayBinder;
    private boolean isCalendarInitialized;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    void initFragment() {
        viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        viewModel.getMonth().observe(this, this::displayCurrentMonth);
        viewModel.setLoggedTimeDao(getLoggedTimeDao());
        viewModel.loadCalendar();

        binding.viewCalendar.buttonLeft.setOnClickListener(this);
        binding.viewCalendar.buttonRight.setOnClickListener(this);

        binding.menuWorkingTime.setOnClickListener(view -> enterLoggedTime(TimeType.Work));
        binding.menuOvertime.setOnClickListener(view -> enterLoggedTime(TimeType.Overtime));
        binding.menuSickLeave.setOnClickListener(view -> enterLoggedTime(TimeType.SickTimeOff));
        binding.menuTimeOff.setOnClickListener(view -> enterLoggedTime(TimeType.TimeOff));
        binding.menuUnpaidTimeOff.setOnClickListener(view -> enterLoggedTime(TimeType.UnpaidTimeOff));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_left) {
            viewModel.prevMonth();
            resetSelectedDay();
        }
        if (view.getId() == R.id.button_right) {
            viewModel.nextMonth();
            resetSelectedDay();
        }
    }

    @Override
    public void onDayClicked(Day day) {
        viewModel.setSelectedDay(day);
        binding.viewCalendar.calendar.notifyCalendarChanged();
        displaySelectedDay(day);

        getLoggedTimeDao().getLoggedTimeByDateLiveData(day.getDay()).observe(
                this, loggedTime -> showLoggedTime(day, loggedTime)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Logged time
     */

    private void enterLoggedTime(TimeType type) {
        binding.fab.toggle(true);
        // todo show input dialog

        // todo: after input data update selected day
//        updateSelectedDay(true);

        LocalDate date = viewModel.getCurrentDay().getDay();
        InputTimeDialog dialog = new InputTimeDialog(getContext(), date, type, getLoggedTimeDao());
        dialog.show();
    }

    private void showLoggedTime(Day day, LoggedTime loggedTime) {
        if (loggedTime == null && !day.isHoliday()) {
            binding.viewInfo.viewNoData.setVisibility(View.VISIBLE);
        } else {
            binding.viewInfo.viewNoData.setVisibility(View.GONE);
        }

        updateHolidayInfo(day);
        updateWorkHoursInfo(loggedTime);
        updateOvertimeInfo(loggedTime);
        updateSickLeaveInfo(loggedTime);
        updateTimOffInfo(loggedTime);
        updateUnpaidTimOffInfo(loggedTime);
    }

    private void updateHolidayInfo(Day day) {
        if (day.isHoliday()) {
            binding.viewInfo.textHoliday.setVisibility(View.VISIBLE);
            String formattedStr = getString(R.string.holiday, day.getHoliday());
            binding.viewInfo.textHoliday.setText(formattedStr);
        } else {
            binding.viewInfo.textHoliday.setVisibility(View.GONE);
        }
    }

    private void updateWorkHoursInfo(LoggedTime loggedTime) {
        if (loggedTime != null && loggedTime.getWork() != 0) {
            binding.viewInfo.textWorkingHours.setVisibility(View.VISIBLE);
            binding.viewInfo.textWorkingHours.setText(
                    getString(R.string.work_hours, loggedTime.getWork())
            );
        } else {
            binding.viewInfo.textWorkingHours.setVisibility(View.GONE);
        }
    }

    private void updateOvertimeInfo(LoggedTime loggedTime) {
        if (loggedTime != null && loggedTime.getOvertime() != 0) {
            binding.viewInfo.textOvertimeHours.setVisibility(View.VISIBLE);
            binding.viewInfo.textOvertimeHours.setText(
                    getString(R.string.overtime_hours, loggedTime.getOvertime())
            );
        } else {
            binding.viewInfo.textOvertimeHours.setVisibility(View.GONE);
        }
    }

    private void updateSickLeaveInfo(LoggedTime loggedTime) {
        if (loggedTime != null && loggedTime.getSickLeave() != 0) {
            binding.viewInfo.textSickLeaveHours.setVisibility(View.VISIBLE);
            binding.viewInfo.textSickLeaveHours.setText(
                    getString(R.string.time_off_sick_hours, loggedTime.getSickLeave())
            );
        } else {
            binding.viewInfo.textSickLeaveHours.setVisibility(View.GONE);
        }
    }

    private void updateTimOffInfo(LoggedTime loggedTime) {
        if (loggedTime != null && loggedTime.getTimeOff() != 0) {
            binding.viewInfo.textTimeOff.setVisibility(View.VISIBLE);
            binding.viewInfo.textTimeOff.setText(
                    getString(R.string.time_off_hours, loggedTime.getTimeOff())
            );
        } else {
            binding.viewInfo.textTimeOff.setVisibility(View.GONE);
        }
    }

    private void updateUnpaidTimOffInfo(LoggedTime loggedTime) {
        if (loggedTime != null && loggedTime.getUnpaidTimeOff() != 0) {
            binding.viewInfo.textUnpaidTimeOff.setVisibility(View.VISIBLE);
            binding.viewInfo.textUnpaidTimeOff.setText(
                    getString(R.string.unpaid_time_off_hours, loggedTime.getUnpaidTimeOff())
            );
        } else {
            binding.viewInfo.textUnpaidTimeOff.setVisibility(View.GONE);
        }
    }

    private void updateSelectedDay(boolean hasRecord) {
        Day day = viewModel.getSelectedDay();
        day.setHasRecord(hasRecord);
        binding.viewCalendar.calendar.notifyCalendarChanged();
    }

    /**
     * Selected month
     */

    private void displayCurrentMonth(Month month) {
        // month name
        String title = getString(R.string.coma_separated, month.getName(), month.getYear());
        binding.viewCalendar.textMonthName.setText(title);
        // moth days info
        String daysInfo = getString(
                R.string.month_days_info,
                month.getHolidayDaysCount(),
                month.getWorkingDaysCount()
        );
        binding.textMonthDaysInfo.setText(daysInfo);

        if (!isCalendarInitialized) {
            dayBinder = new CalendarDayBinder(viewModel.getCalendar().getValue(), this);

            binding.viewCalendar.calendar.setDayBinder(dayBinder);
            binding.viewCalendar.calendar.setMonthHeaderBinder(new MonthHeaderBinder());
            binding.viewCalendar.calendar.setup(
                    YearMonth.of(YearMonth.now().getYear(), month.getIndex()),
                    YearMonth.of(YearMonth.now().getYear(), month.getIndex()),
                    DayOfWeek.MONDAY
            );
            dayBinder.setSelectedDay(viewModel.getCurrentDay());
            isCalendarInitialized = true;
        } else {
            binding.viewCalendar.calendar.updateMonthRange(
                    YearMonth.of(YearMonth.now().getYear(), month.getIndex()),
                    YearMonth.of(YearMonth.now().getYear(), month.getIndex())
            );
        }
    }

    /**
     * Selected day
     */

    private void displaySelectedDay(Day day) {
        DayOfWeek dayOfWeek = day.getDay().getDayOfWeek();
        String dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
        String dayNameCapitalized = StringUtils.capitalize(dayName);
        String monthName = day.getDay().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String monthNameCapitalized = StringUtils.capitalize(monthName);

        String textDay = getString(
                R.string.selected_day,
                dayNameCapitalized,
                day.getDay().getDayOfMonth(),
                monthNameCapitalized,
                day.getDay().getYear()
        );

        setTitle(textDay);
    }

    private void resetSelectedDay() {
        dayBinder.resetSelectedDay();
        super.setTitle(getString(R.string.app_name));
    }

}