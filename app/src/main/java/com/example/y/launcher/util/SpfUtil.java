package com.example.y.launcher.util;

import android.content.Context;

import com.example.y.launcher.App;

public class SpfUtil {

    public static Context getContext(){
        return App.getInstance().getContext();
    }
    public static String getString(String key, String defValue) {
        return getContext()
                .getSharedPreferences("wifi_spf", Context.MODE_PRIVATE)
                .getString(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return getContext()
                .getSharedPreferences("wifi_spf", Context.MODE_PRIVATE)
                .getInt(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getContext()
                .getSharedPreferences("wifi_spf", Context.MODE_PRIVATE)
                .getBoolean(key, defValue);
    }

    public static void putString(String key, String value) {
        getContext().getSharedPreferences("wifi_spf", Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static void putInt(String key, int value) {
        getContext().getSharedPreferences("wifi_spf", Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public static void putBoolean(String key, boolean value) {
        getContext().getSharedPreferences("wifi_spf", Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }}
