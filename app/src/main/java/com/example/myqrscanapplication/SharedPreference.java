package com.example.myqrscanapplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yatra on 17/4/16.
 */
public class SharedPreference {

    private static final String PREF_NAME = "travelmatch";
    public static final int MODE = Context.MODE_PRIVATE;

    public static final String LOGIN_CHECK_KEY = "user_login";
    public static final String USER_NUMBER = "user_number";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_NAME = "user_name";

    public static String getString(Context context,String key, String def) {
        return getPreferences(context).getString(key,def);
    }

    public static boolean getBoolean(Context context,String key,boolean def) {
        return getPreferences(context).getBoolean(key, def);
    }

    public static void setString(Context context,String key,String defValue) {
        getEditor(context).putString(key,defValue).commit();
    }

    public static void setBoolean(Context context,String key,boolean value) {
        getEditor(context).putBoolean(key,value).apply();
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

}
