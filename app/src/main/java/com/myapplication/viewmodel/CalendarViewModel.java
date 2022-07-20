package com.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myapplication.config.Global;
import com.myapplication.data.Calendar;
import com.myapplication.data.Day;
import com.myapplication.data.LoggedTime;
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

/**
 * Обект който съдържа логиката за Календара
 */
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

    // връща текущият ден от месеца
    public Day getCurrentDay() {
        Day day = _month.getValue().getDay(this.nowDayIndex);
        day.setIsSelected(true);
        return day;
    }

    // връща текущият селектиран ден
    public Day getSelectedDay() {
        return this.selectedDay;
    }

    // запазва кой е текущият селектиран ден
    public void setSelectedDay(Day day) {
        this.selectedDay = day;
    }

    /**
     * Данни за текушият месец които се променят автоматично когато има нови записи
     */
    private final int nowMonthIndex = LocalDate.now().getMonthValue() - 1;
    private int currentMonthIndex = nowMonthIndex;

    private final MutableLiveData<Month> _month = new MutableLiveData<>();

    public LiveData<Month> getMonth() {
        return this._month;
    }

    /**
     * Фунцкия която зарежда предишният месец
     * като първо проверява дали има такъв, ако няма не се случва нищо
     */
    public void nextMonth() {
        Calendar calendar = _calendar.getValue();
        if (calendar == null) return;

        if (currentMonthIndex < calendar.getMonths().size() - 1) {
            currentMonthIndex++;
            _month.postValue(calendar.getMonths().get(currentMonthIndex));
        }
    }

    /**
     * Фунцкия която зарежда следващият месец
     * като първо проверява дали има такъв, ако няма не се случва нищо
     */
    public void prevMonth() {
        Calendar calendar = _calendar.getValue();
        if (calendar == null) return;

        if (currentMonthIndex > 0) {
            currentMonthIndex--;
            _month.postValue(calendar.getMonths().get(currentMonthIndex));
        }
    }

    /**
     * Календар данни които се променят автоматично когато има нови записи
     */
    private final MutableLiveData<Calendar> _calendar = new MutableLiveData<>();

    public LiveData<Calendar> getCalendar() {
        return this._calendar;
    }

    // функция която зарежда календара
    public void loadCalendar() {
        Integer year = LocalDate.now().getYear();
        Calendar calendar = getCalendarDays(year);
        _calendar.postValue(calendar);

        if (calendar != null) {
            _month.postValue(calendar.getMonths().get(currentMonthIndex));
        }
    }

    // връща календар обект като използва данни от kik-info.com
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

    // връща календар обект спрямо годината
    private Calendar getCalendar(Document document, Integer year) {
        ArrayList<Month> months = new ArrayList<>();

        for (int monthIndex = 1; monthIndex <= 12; monthIndex++) {
            Month month = getMonth(document, year, monthIndex);
            months.add(month);
        }

        return new Calendar(year, months);
    }

    // връща дедаен месец с данни за него
    // приема страница от kik-info.com, година и индекс на месеца
    private Month getMonth(Document document, Integer year, Integer monthIndex) {
        String CALENDAR_ID = "card cal-month m_";
        ArrayList<Day> days = new ArrayList<>();

        Elements elements = document.getElementsByClass(CALENDAR_ID + monthIndex);
        List<Node> nodes = elements.get(0).childNodes();
        List<Node> monthNodes = nodes.get(1).childNodes().get(0).childNodes();

        // цикъл който обикаля всички месеци в страницата
        for (int index = 1; index < monthNodes.size(); index++) {
            List<Node> rowNodes = monthNodes.get(index).childNodes();

            // цикъл който обикаля всички редове в таблицата за даден месец
            for (int rowIndex = 0; rowIndex < rowNodes.size(); rowIndex++) {
                Element rowElement = (Element) rowNodes.get(rowIndex);
                if (rowElement.text().isEmpty()) continue;

                // взима числото на деня
                String dayValue = rowElement.text();
                // взима това дали деня е почивен или не
                boolean isNotWorkingDay = rowElement.className().contains("table-active");
                // взима името на празника, ако има такъв
                String holidayTitle = rowElement.attributes().get("title");

                // генерира обект с месеца
                java.time.Month month = java.time.Month.of(monthIndex);
                // генерира дата която ще се използва в приложението
                LocalDate date = LocalDate.of(year, month, Integer.parseInt(dayValue));

                // взима обект с логнатите часове да конкретната дата
                LoggedTime loggedTime = loggedTimeDao.getLoggedTimeByDate(date);
                // проверява дали за тази дата има записани данни
                boolean hasRecord = loggedTime != null && loggedTime.hasData();
                // генерира обект за деня който се използва в приложението
                Day day = new Day(date, isNotWorkingDay, holidayTitle, hasRecord);
                // добавя деня в масива
                days.add(day);
            }
        }

        // връща месец с индекса от годината и неговите дни
        return new Month(monthIndex, days);
    }

}
