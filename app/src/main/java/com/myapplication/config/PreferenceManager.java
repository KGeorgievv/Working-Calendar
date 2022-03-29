package com.myapplication.config;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private final SharedPreferences sharedPref;

    PreferenceManager(Context context) {
        sharedPref = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    boolean hasUser() {
        return this.sharedPref.getBoolean(Global.PREF_HAS_USER, false);
    }

}
