package com.myapplication.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.myapplication.data.User;

/**
 * Обект който пази локално данните за потребителя
 */
public class PreferenceManager {

    private final SharedPreferences sharedPref;

    public PreferenceManager(Context context) {
        sharedPref = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    // запазва данните за потребител
    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(Global.PREF_SALARY, user.getSalary());
        editor.putInt(Global.PREF_PTO_DAYS, user.getPtoDays());
        editor.apply();
    }

    // връща дали има запазен потребител
    // като проверява дали има въведена заплата и брой почивни дни
    public boolean hasUser() {
        boolean hasSalary = sharedPref.contains(Global.PREF_SALARY);
        boolean hasPtoDays = sharedPref.contains(Global.PREF_PTO_DAYS);
        return hasSalary && hasPtoDays;
    }

    // връща текущият поретибел който е запазен
    public User getUser() {
        if (!hasUser()) throw new IllegalStateException("No user found!");
        float salary = sharedPref.getFloat(Global.PREF_SALARY, 0);
        int ptoDays = sharedPref.getInt(Global.PREF_PTO_DAYS, 0);
        return new User(salary, ptoDays);
    }

    // изтрива данните за потребителя
    public void clear() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Global.PREF_SALARY);
        editor.remove(Global.PREF_PTO_DAYS);
        editor.apply();
    }

}
