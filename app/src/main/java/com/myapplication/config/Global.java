package com.myapplication.config;

// Глобален обект който съдържа конфигурации
public class Global {

    // уебсайт от който се взимат данните за календара
    private static final String API_CALENDAR = "https://kik-info.com/spravochnik/calendar/";

    // ключ с който се пазят/взимат данните за въведената заплата
    public static final String PREF_SALARY = "pref_salary";
    // ключ с който се пазят/взимат данните за въведените почивни дни
    public static final String PREF_PTO_DAYS = "pref_pto_days";

    // фунцкяи която връща url-а спрямо въведената година
    public static String getApiCalendarForYear(Integer year) {
        return API_CALENDAR + year;
    }

}
