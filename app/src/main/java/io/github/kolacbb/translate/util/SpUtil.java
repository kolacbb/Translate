package io.github.kolacbb.translate.util;

import android.content.SharedPreferences;

/**
 * Created by Kola on 2016/5/16.
 */
public class SpUtil {

    private static SharedPreferences mSp;

    public static void init(SharedPreferences sp) {
        mSp = sp;
    }

    public static void save(String key, String value) {
        mSp.edit().putString(key, value).apply();
    }

    public static void save(String key, Boolean value) {
        mSp.edit().putBoolean(key, value).apply();
    }

    public static String findString(String key) {
        return mSp.getString(key, null);
    }

    public static boolean findBoolean(String key) {
        return mSp.getBoolean(key, false);
    }

    public static void delete(String key) {
        mSp.edit().remove(key).apply();
    }

    public static void clearAll() {
        mSp.edit().clear().apply();
    }
}