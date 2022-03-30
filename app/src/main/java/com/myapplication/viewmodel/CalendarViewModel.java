package com.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myapplication.config.Global;
import com.myapplication.data.Calendar;
import com.myapplication.data.Day;
import com.myapplication.data.Month;
import com.myapplication.database.LoggedTimeDao;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarViewModel extends ViewModel {

    private LoggedTimeDao loggedTimeDao;

    public void setLoggedTimeDao(LoggedTimeDao loggedTimeDao) {
        this.loggedTimeDao = loggedTimeDao;
    }

    /**
     * Days
     */
    private final int nowDayIndex = LocalDate.now().getDayOfMonth() - 1;

    private Day selectedDay;

    public Day getCurrentDay() {
        Day day = _month.getValue().getDay(this.nowDayIndex);
        day.setIsSelected(true);
        return day;
    }

    public Day getSelectedDay() {
        return this.selectedDay;
    }

    public void setSelectedDay(Day day) {
        this.selectedDay = day;
    }

    /**
     * Month calendar
     */
    private final int nowMonthIndex = LocalDate.now().getMonthValue() - 1;
    private int currentMonthIndex = nowMonthIndex;

    private final MutableLiveData<Month> _month = new MutableLiveData<>();

    public LiveData<Month> getMonth() {
        return this._month;
    }

    public void nextMonth() {
        Calendar calendar = _calendar.getValue();
        if (calendar == null) return;

        if (currentMonthIndex < calendar.getMonths().size() - 1) {
            currentMonthIndex++;
            _month.postValue(calendar.getMonths().get(currentMonthIndex));
        }
    }

    public void prevMonth() {
        Calendar calendar = _calendar.getValue();
        if (calendar == null) return;

        if (currentMonthIndex > 0) {
            currentMonthIndex--;
            _month.postValue(calendar.getMonths().get(currentMonthIndex));
        }
    }

    /**
     * Year calendar
     */
    private final MutableLiveData<Calendar> _calendar = new MutableLiveData<>();

    public LiveData<Calendar> getCalendar() {
        return this._calendar;
    }

    public void loadCalendar() {
        Integer year = LocalDate.now().getYear();
        Calendar calendar = getCalendarDays(year);
        _calendar.postValue(calendar);

        if (calendar != null) {
            _month.postValue(calendar.getMonths().get(currentMonthIndex));
        }
    }

    private Calendar getCalendarDays(Integer year) {
        try {
            String url = Global.getApiCalendarForYear(year);
            Document document = Jsoup.connect(url).get();
            return getCalendar(document, year);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Calendar getCalendar(Document document, Integer year) {
        ArrayList<Month> months = new ArrayList<>();

        for (int monthIndex = 1; monthIndex <= 12; monthIndex++) {
            Month month = getMonth(document, year, monthIndex);
            months.add(month);
        }

        return new Calendar(year, months);
    }

    private Month getMonth(Document document, Integer year, Integer monthIndex) {
        String CALENDAR_ID = "card cal-month m_";
        ArrayList<Day> days = new ArrayList<>();

        Elements elements = document.getElementsByClass(CALENDAR_ID + monthIndex);
        List<Node> nodes = elements.get(0).childNodes();
        List<Node> monthNodes = nodes.get(1).childNodes().get(0).childNodes();

        for (int index = 1; index < monthNodes.size(); index++) {
            List<Node> rowNodes = monthNodes.get(index).childNodes();

            for (int rowIndex = 0; rowIndex < rowNodes.size(); rowIndex++) {
                Element rowElement = (Element) rowNodes.get(rowIndex);
                if (rowElement.text().isEmpty()) continue;

                String dayValue = rowElement.text();
                boolean isNotWorkingDay = rowElement.className().contains("table-active");
                String holidayTitle = rowElement.attributes().get("title");

                java.time.Month month = java.time.Month.of(monthIndex);
                LocalDate date = LocalDate.of(year, month, Integer.parseInt(dayValue));

                boolean hasRecord = loggedTimeDao.getLoggedTimeByDate(date) != null;
                Day day = new Day(date, isNotWorkingDay, holidayTitle, hasRecord);
                days.add(day);
            }
        }

        return new Month(monthIndex, days);
    }

}
