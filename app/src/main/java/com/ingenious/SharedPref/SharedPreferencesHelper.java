package com.ingenious.SharedPref;

import android.content.SharedPreferences;

import com.ingenious.hdwallpapers.Activity.MyApplication;


/**
 * Helper for work with SharedPreferences
 */
public class SharedPreferencesHelper {

    private final static String PREF_FILE = "MyPrefs";

    /**
     * Save data to shared preferences
     * @param context context
     * @param key
     * @param value
     */
    public static void setString(String key, String value){
        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Save integer to shared preferences
     * @param context context
     * @param key ключ
     * @param value значение
     */
    public static void setInt(String key, int value){
        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Save boolean to shared preferences
     * @param context context
     * @param key
     * @param value
     */
    public static void setBoolean(String key, boolean value){
        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * get value shared preferences
     * @param key
     * @param defValue
     * @return string
     */
    public static String getString(String key, String defValue){
        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(PREF_FILE, 0);
        return settings.getString(key, defValue);
    }

    /**
     * get int из sharedpreferences
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(String key, int defValue){
        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(PREF_FILE, 0);
        return settings.getInt(key, defValue);
    }

    /**
     * get Boolean from shared preferences
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean( String key, boolean defValue){
        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(PREF_FILE, 0);
        return settings.getBoolean(key, defValue);
    }

    /**
     * remove from shared prefs
     * @param key
     */
    public static void remove(String key){
        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(PREF_FILE, 0);
        settings.edit().remove(key).apply();
    }
}
