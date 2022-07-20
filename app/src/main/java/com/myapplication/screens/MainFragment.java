package com.myapplication.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.myapplication.R;
import com.myapplication.config.PreferenceManager;
import com.myapplication.data.Day;
import com.myapplication.data.LoggedTime;
import com.myapplication.data.Month;
import com.myapplication.data.MonthSalary;
import com.myapplication.data.TimeType;
import com.myapplication.data.User;
import com.myapplication.databinding.FragmentMainBinding;
import com.myapplication.viewmodel.CalendarViewModel;
import com.myapplication.views.CalendarDayBinder;
import com.myapplication.views.CalendarListener;
import com.myapplication.views.ExitAppDialog;
import com.myapplication.views.InputTimeDialog;
import com.myapplication.views.MonthHeaderBinder;

import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

// Основният екран на приложението
public class MainFragment extends BaseFragment implements View.OnClickListener, CalendarListener,
        InputTimeDialog.Listener, ExitAppDialog.Listener {

    private FragmentMainBinding binding;
    private CalendarViewModel viewModel;
    private boolean isCalendarInitialized;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // създаване на изгледа
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    void initFragment() {
        // добавяне на опция екрана да им меню
        setHasOptionsMenu(true);

        preferenceManager = new PreferenceManager(getContext());

        // създаване на обекта който е отговорен за зареждането на данните на екрана
        viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        // добавяне на логика за това кога даден месец се променя
        viewModel.getMonth().observe(this, this::displayCurrentMonth);
        // добавяне на обекта за взимане на записани часове
        viewModel.setLoggedTimeDao(getLoggedTimeDao());
        // зареждане на календара
        viewModel.loadCalendar();

        // добавяне на логика когато се натисне бутона за предишен месец
        binding.viewCalendar.buttonLeft.setOnClickListener(this);
        // добавяне на логика когато се натисне бутона за следващ месец
        binding.viewCalendar.buttonRight.setOnClickListener(this);

        // добавяне на логика коато се натисне бутона за работни часове
        binding.menuWorkingTime.setOnClickListener(view -> enterLoggedTime(TimeType.Work));
        // добавяне на логика коато се натисне бутона за извънредни часове
        binding.menuOvertime.setOnClickListener(view -> enterLoggedTime(TimeType.Overtime));
        // добавяне на логика коато се натисне бутона за болнични часове
        binding.menuSickLeave.setOnClickListener(view -> enterLoggedTime(TimeType.SickTimeOff));
        // добавяне на логика коато се натисне бутона за часове за отпуска
        binding.menuTimeOff.setOnClickListener(view -> enterLoggedTime(TimeType.TimeOff));
        // добавяне на логика коато се натисне бутона за неплатена отпуска
        binding.menuUnpaidTimeOff.setOnClickListener(view -> enterLoggedTime(TimeType.UnpaidTimeOff));
    }

    @Override
    public void onClick(View view) {
        // бутон за предишен месец
        if (view.getId() == R.id.button_left) viewModel.prevMonth();
        // бутон за следващ месец
        if (view.getId() == R.id.button_right) viewModel.nextMonth();
    }

    // фунцкия която се извиква когато се избере даден ден
    @Override
    public void onDayClicked(Day day) {
        // запазване на избрания ден
        viewModel.setSelectedDay(day);
        // ъпдейтване на календара
        binding.viewCalendar.calendar.notifyCalendarChanged();
        // показване на данните от избрание ден
        displaySelectedDay(day);

        // взимане на записаните данни за избрания ден
        getLoggedTimeDao().getLoggedTimeByDateLiveData(day.getDay()).observe(
                this, loggedTime -> showLoggedTime(day, loggedTime)
        );
    }

    /**
     * Фунцкия която се извиква когато са въведени часове за даден ден
     */
    @Override
    public void onLoggedTime(LocalDate date) {
        // взимане на въведените часове
        LoggedTime loggedTime = getLoggedTimeDao().getLoggedTimeByDate(date);
        // проверка дали има въведени данни
        boolean hasRecord = loggedTime != null && loggedTime.hasData();
        // ъпдейтване на текущият ден
        updateSelectedDay(hasRecord);
    }

    /**
     * Фунцкия която се извиква когато менюто се създава (опция "изход")
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    /**
     * Фунцкия която се извиква когато се избере опцията "изход"
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {
            ExitAppDialog dialog = new ExitAppDialog(getContext());
            dialog.setListener(this);
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Фунцкия която се извиква когато потребителя иска да излезне от приложението
     */
    @Override
    public void onExit() {
        // изтриване на записаните часове
        getLoggedTimeDao().clear();
        // изтриване на потребителя
        preferenceManager.clear();

        // навигиране към екрана за въвеждане на заплата и почивни дни
        NavOptions.Builder navOptions = new NavOptions.Builder();
        navOptions.setPopUpTo(R.id.action_main, true);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_initialize, null, navOptions.build());
    }

    /**
     * Фунцкия която се извиква когато екрана се изтрие от паметта
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Фунцкия която изкарва диалог за въвеждане на данни
     */
    private void enterLoggedTime(TimeType type) {
        binding.fab.toggle(true);

        // взимане на текущата дата
        LocalDate date = viewModel.getSelectedDay().getDay();
        // създаване на диалога
        InputTimeDialog dialog = new InputTimeDialog(getContext(), date, type, getLoggedTimeDao());
        // добавяне на интерфейс който ще слуша за промяната
        dialog.setListener(this);
        // показване на диалога
        dialog.show();
    }

    /**
     * Фунцкия която показва информацията за въведени часове за избран ден + празник, ако има такъв
     * Ако няма данни се показва изглед който индикира, че няма данни
     * Ако има данни се показват данните за деня
     */
    private void showLoggedTime(Day day, LoggedTime loggedTime) {
        if (loggedTime == null || !loggedTime.hasData()) {
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

    /**
     * Фунцкия която показва или скирва информацията за почивен ден
     */
    private void updateHolidayInfo(Day day) {
        if (day.isHoliday()) {
            binding.viewInfo.textHoliday.setVisibility(View.VISIBLE);
            String formattedStr = getString(R.string.holiday, day.getHoliday());
            binding.viewInfo.textHoliday.setText(formattedStr);
        } else {
            binding.viewInfo.textHoliday.setVisibility(View.GONE);
        }
    }

    /**
     * Фунцкия която показва или скрива информацията за работни часове за конкретната дата
     */
    private void updateWorkHoursInfo(LoggedTime loggedTime) {
        if (loggedTime != null && loggedTime.getWork() != 0) {
            binding.viewInfo.textWorkingHours.setVisibility(View.VISIBLE);
            binding.viewInfo.textWorkingHours.setText(
                    getString(
                            // форматиращ текст
                            R.string.work_hours,
                            // работни часове
                            loggedTime.getWork()
                    )
            );
        } else {
            binding.viewInfo.textWorkingHours.setVisibility(View.GONE);
        }
    }

    /**
     * Фунцкия която показва или скрива информацията за извънредни часове за конкретната дата
     */
    private void updateOvertimeInfo(LoggedTime loggedTime) {
        if (loggedTime != null && loggedTime.getOvertime() != 0) {
            binding.viewInfo.textOvertimeHours.setVisibility(View.VISIBLE);
            binding.viewInfo.textOvertimeHours.setText(
                    getString(
                            // форматиращ текст
                            R.string.overtime_hours,
                            // извънредни часове
                            loggedTime.getOvertime()
                    )
            );
        } else {
            binding.viewInfo.textOvertimeHours.setVisibility(View.GONE);
        }
    }

    /**
     * Фунцкия която показва или скрива информацията за болнични часове за конкретната дата
     */
    private void updateSickLeaveInfo(LoggedTime loggedTime) {
        if (loggedTime != null && loggedTime.getSickLeave() != 0) {
            binding.viewInfo.textSickLeaveHours.setVisibility(View.VISIBLE);
            binding.viewInfo.textSickLeaveHours.setText(
                    getString(
                            // форматиращ текст
                            R.string.time_off_sick_hours,
                            // болнични часове за конкретната дата
                            loggedTime.getSickLeave()
                    )
            );
        } else {
            binding.viewInfo.textSickLeaveHours.setVisibility(View.GONE);
        }
    }

    /**
     * Фунцкия която показва или скрива информацията за почивни дни за конкретната дата
     */
    private void updateTimOffInfo(LoggedTime loggedTime) {
        if (loggedTime != null && loggedTime.getTimeOff() != 0) {
            binding.viewInfo.textTimeOff.setVisibility(View.VISIBLE);
            binding.viewInfo.textTimeOff.setText(
                    getString(
                            // форматиращ текст
                            R.string.time_off_hours,
                            // почивни часове
                            loggedTime.getTimeOff()
                    )
            );
        } else {
            binding.viewInfo.textTimeOff.setVisibility(View.GONE);
        }
    }

    /**
     * Фунцкия която показва или скрива информацията за неплатени часове за конкретната дата
     */
    private void updateUnpaidTimOffInfo(LoggedTime loggedTime) {
        if (loggedTime != null && loggedTime.getUnpaidTimeOff() != 0) {
            binding.viewInfo.textUnpaidTimeOff.setVisibility(View.VISIBLE);
            binding.viewInfo.textUnpaidTimeOff.setText(
                    getString(
                            // форматиращ текст
                            R.string.unpaid_time_off_hours,
                            // неплатени часове
                            loggedTime.getUnpaidTimeOff()
                    )
            );
        } else {
            binding.viewInfo.textUnpaidTimeOff.setVisibility(View.GONE);
        }
    }

    /**
     * Фунцкия която ъпдейтва дали даден ден има записи или не
     */
    private void updateSelectedDay(boolean hasRecord) {
        Day day = viewModel.getSelectedDay();
        day.setHasRecord(hasRecord);
        binding.viewCalendar.calendar.notifyCalendarChanged();
    }

    /**
     * Функция която показва календара
     */
    private void displayCurrentMonth(Month month) {
        // заглавие - името на месеца и годината
        String title = getString(R.string.coma_separated, month.getName(), month.getYear());
        binding.viewCalendar.textMonthName.setText(title);
        // допълнителен текст - почивни и работни дни в месеца
        String daysInfo = getString(
                R.string.month_days_info,
                month.getHolidayDaysCount(),
                month.getWorkingDaysCount()
        );
        binding.textMonthDaysInfo.setText(daysInfo);

        // ако календара не е бил създаден
        if (!isCalendarInitialized) {
            // създаване на обекта който се грижи за показването на дните в календара
            CalendarDayBinder dayBinder = new CalendarDayBinder(viewModel.getCalendar().getValue(), this);
            binding.viewCalendar.calendar.setDayBinder(dayBinder);

            // създаване на обекта който се грижи за показването на дните в седмицата
            binding.viewCalendar.calendar.setMonthHeaderBinder(new MonthHeaderBinder());
            // създаване на календара като се подава текущият месец и година
            binding.viewCalendar.calendar.setup(
                    YearMonth.of(YearMonth.now().getYear(), month.getIndex()),
                    YearMonth.of(YearMonth.now().getYear(), month.getIndex()),
                    DayOfWeek.MONDAY
            );
            // запазване на текущият ден от месеца
            dayBinder.setSelectedDay(viewModel.getCurrentDay());
            // запазване на това, че календара е бил създаден вече
            isCalendarInitialized = true;
            // ако календара е вече създаден само се ъпдейтва
        } else {
            binding.viewCalendar.calendar.updateMonthRange(
                    YearMonth.of(YearMonth.now().getYear(), month.getIndex()),
                    YearMonth.of(YearMonth.now().getYear(), month.getIndex())
            );
        }

        // зареждане на датите от месеца и взимане на записание часове за тях
        List<LocalDate> dates = month.getDays();
        getLoggedTimeDao().getLoggedTimeForDates(dates).observe(this, this::displaySalaryData);
    }


    /**
     * Функция която променя заглавието на екрана за даден ден
     */
    private void displaySelectedDay(Day day) {
        // взимане на деня от седмицата
        DayOfWeek dayOfWeek = day.getDay().getDayOfWeek();
        // генериране на името на деня
        String dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
        // генериране на имтео на деня да започва с главна буква
        String dayNameCapitalized = StringUtils.capitalize(dayName);
        // генериране на името на месеца
        String monthName = day.getDay().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        // генериране на името на месеца да започва с главна буква
        String monthNameCapitalized = StringUtils.capitalize(monthName);

        // генериране на заглавието
        String textDay = getString(
                // форматиращ текс
                R.string.selected_day,
                // името на деня с главна буква
                dayNameCapitalized,
                // числото на деня от месеца
                day.getDay().getDayOfMonth(),
                // името на месеца с главна буква
                monthNameCapitalized,
                // годината
                day.getDay().getYear()
        );

        // показване на заглавието
        setTitle(textDay);
    }

    /**
     * Фунцкия която показва не данните за текущият месец
     */
    private void displaySalaryData(List<LoggedTime> loggedTimes) {
        // зареждане на данните за потребителя
        PreferenceManager preferenceManager = new PreferenceManager(getContext());
        User user = preferenceManager.getUser();

        // генериране на обект съдържащ данните за дадение потребител
        MonthSalary salary = new MonthSalary(user, viewModel.getMonth().getValue(), loggedTimes);

        // показване на данните
        binding.textSalaryInfo.setText(
                String.format(
                        // форматиращ текст
                        getString(R.string.salary),
                        // данни за заплата на час
                        salary.getSalaryPerHour(),
                        // данни за калкулираната текуща заплата
                        salary.calculateSalary(),
                        // данни за цялата заплата за месеца
                        salary.getMonthSalary()
                )
        );
    }

}