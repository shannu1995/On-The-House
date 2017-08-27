package com.onthehouse.Utils;

/**
 * Created by anashanifm on 28/8/17.
 * Initalize object with context and sharedpreference key
 *      --SharedPreferenceClass spc = new SharedPreferenceClass(this, key);
 * Put String, int, float, boolean, String set
 *      --- spc.putString(key, value);
 * Get String, int, float, boolean, String set
 *      --- spc.getString(key);
 *
 * Use these 3 functions in class for shared preferences
 */

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Shared Preference class
 *
 */
public class SharedPreferenceClass {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    /**
     *
     * @param context = Context of activity
     * @param sharedPreferenceKey = key for sharedpreference
     * Initialize shared preference
     */
    public SharedPreferenceClass(Context context, String sharedPreferenceKey)
    {
        sharedPreferences = context.getSharedPreferences(sharedPreferenceKey, Context.MODE_PRIVATE);
    }

    /**
     *
     * @param key
     * @param value
     * String values in sharedpreference
     */
    public void putString(String key, String value)
    {
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     *
     * @param key
     * @param value
     * Int values in sharedpreference
     */
    public void putInt(String key, int value)
    {
        editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     *
     * @param key
     * @param value
     * float values in sharedpreference
     */
    public void putFloat(String key, float value)
    {
        editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     *
     * @param key
     * @param value
     * boolean value in sharedpreference
     */
    public void putBoolean(String key, boolean value)
    {
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     *
     * @param key
     * @param value
     * String set values in sharedpreference
     */
    public void putStringSet(String key, Set<String> value)
    {
        editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    /**
     *
     * @param key
     * @return string, return null if key not found
     */
    public String getString(String key)
    {
        return sharedPreferences.getString(key, "null");
    }

    /**
     *
     * @param key
     * @return int, 0 if key not found
     */
    public int getInt(String key)
    {
        return sharedPreferences.getInt(key, 0);
    }

    /**
     *
     * @param key
     * @return float, 0 if key not found
     */
    public float getFloat(String key)
    {
        return sharedPreferences.getFloat(key, 0);
    }

    /**
     *
     * @param key
     * @return boolean, false if key not found
     */
    public boolean getBoolean(String key)
    {
        return sharedPreferences.getBoolean(key, false);
    }

    /**
     *
     * @param key
     * @return string set, null if key not found
     */
    public Set<String> getStringSet(String key)
    {
        return sharedPreferences.getStringSet(key, null);
    }

}
