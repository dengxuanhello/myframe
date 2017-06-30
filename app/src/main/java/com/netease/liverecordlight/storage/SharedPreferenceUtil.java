package com.netease.liverecordlight.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.netease.liverecordlight.APP;

/**
 * SharedPreference的帮助类
 */
public class SharedPreferenceUtil{

    private final static String SHARED_PREF = "shared_pref";
    private static SharedPreferences getSharedPreferences() {
        SharedPreferences setting = APP.getApp().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return setting;
    }

    /**
     * 保存String类型的Map 值
     *
     * @param key   Map->key
     * @param value Map->value
     */
    public static void setSharedValue(String key, String value) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        try {
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取String类型的Value 值
     *
     * @param key          Map->key
     * @param defaultValue 若没值的情况下的默认值
     * @return
     */
    public static String getSharedValue(String key, String defaultValue) {
        SharedPreferences settings = getSharedPreferences();
        String result = settings.getString(key, defaultValue);
        if(!TextUtils.isEmpty(result)){
            return result;
        }
        return defaultValue;
    }

    public static void setInt(String key, int value) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences settings = getSharedPreferences();
        return settings.getInt(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences settings = getSharedPreferences();
        return settings.getBoolean(key, defaultValue);
    }
}
