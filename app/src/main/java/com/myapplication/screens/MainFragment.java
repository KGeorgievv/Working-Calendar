package com.myapplication.screens;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.myapplication.R;
import com.myapplication.data.Day;
import com.myapplication.data.Month;
import com.myapplication.databinding.FragmentMainBinding;
import com.myapplication.viewmodel.CalendarViewModel;
import com.myapplication.views.CalendarDayBinder;
import com.myapplication.views.CalendarListener;
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
        viewModel.loadCalendar();

        binding.viewCalendar.buttonLeft.setOnClickListener(this);
        binding.viewCalendar.buttonRight.setOnClickListener(this);

        binding.menuWorkingTime.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Working time...", Toast.LENGTH_SHORT).show();
            binding.fab.toggle(true);
        });
        binding.menuOvertime.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Overtime...", Toast.LENGTH_SHORT).show();
            binding.fab.toggle(true);

        });
        binding.menuSickLeave.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Sick leave...", Toast.LENGTH_SHORT).show();
            binding.fab.toggle(true);
        });
        binding.menuLeave.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Leave...", Toast.LENGTH_SHORT).show();
            binding.fab.toggle(true);
        });
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
        binding.viewCalendar.calendar.notifyCalendarChanged();
        displaySelectedDay(day);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

        // update title
        SpannableString span = new SpannableString(textDay);
        int indexOfSpan = textDay.indexOf(dayNameCapitalized);
        int color = ContextCompat.getColor(getContext(), R.color.primary);
        span.setSpan(new ForegroundColorSpan(color), indexOfSpan, textDay.length(), 0);
        setTitle(textDay);
    }

    private void resetSelectedDay() {
        dayBinder.resetSelectedDay();
        super.setTitle(getString(R.string.app_name));
    }

}