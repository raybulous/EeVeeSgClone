package com.example.authapp3.control;

import android.content.Context;
import android.content.SharedPreferences;

public class prefConfig {
    private static final String MY_PREFERENCE_NAME = "com.example.authapp3";
    private static final String PREF_TOTAL_KEY = "pref_total_key";
    private static final String PREF_KEEP_LOGIN_KEY = "pref_keep_login_key";
    private static final String PREF_ALERT_KEY = "pref_alert_key";

    public static void saveTotalInPref(Context context, int total)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_TOTAL_KEY, total);
        editor.apply();
    }

    public static int loadTotalFromPref(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, context.MODE_PRIVATE);
        return pref.getInt(PREF_TOTAL_KEY,0);
    }

    public static void saveKeepLoginInPref(Context context, Boolean check)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_KEEP_LOGIN_KEY, check);
        editor.apply();
    }

    public static Boolean loadKeepLoginFromPref(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, context.MODE_PRIVATE);
        return pref.getBoolean(PREF_KEEP_LOGIN_KEY,false);
    }

    public static void saveAlertInPref(Context context, int previous)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_ALERT_KEY, previous);
        editor.apply();
    }

    public static int loadAlertFromPref(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE_NAME, context.MODE_PRIVATE);
        return pref.getInt(PREF_ALERT_KEY,100);
    }
}
