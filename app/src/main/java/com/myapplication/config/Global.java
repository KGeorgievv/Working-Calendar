package com.myapplication.config;

public class Global {

    // API
    private static final String API_CALENDAR = "https://kik-info.com/spravochnik/calendar/";

    // Shared preferences
    public static final String PREF_SALARY = "pref_salary";
    public static final String PREF_PTO_DAYS = "pref_pto_days";

    public static String getApiCalendarForYear(Integer year) {
        return API_CALENDAR + year;
    }

}
