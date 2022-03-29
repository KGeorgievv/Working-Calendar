package com.myapplication.config;

public class Global {

    // API
    private static final String API_CALENDAR = "https://kik-info.com/spravochnik/calendar/";

    // Shared preferences
    public static final String PREF_HAS_USER = "pref_has_user";

    public static String getApiCalendarForYear(Integer year) {
        return API_CALENDAR + year;
    }

}
