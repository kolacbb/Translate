package io.github.kolacbb.translate.util;

import android.preference.PreferenceManager;

import io.github.kolacbb.translate.inject.component.ApplicationComponent;

/**
 * Created by Kola on 2016/5/16.
 */
public class SpUtil {
    public static void save(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(ApplicationComponent
                .Instance
                .get()
                .getApplication())
                .edit().putString(key, value).apply();
    }

    public static void save(String key, Boolean value) {
        PreferenceManager.getDefaultSharedPreferences(ApplicationComponent
                .Instance
                .get()
                .getApplication())
                .edit().putBoolean(key, value).apply();
    }

    public static String findString(String key) {
        return PreferenceManager.getDefaultSharedPreferences(ApplicationComponent
                .Instance
                .get()
                .getApplication()).getString(key, null);
    }

    public static boolean findBoolean(String key) {
        return PreferenceManager.getDefaultSharedPreferences(ApplicationComponent
                .Instance
                .get()
                .getApplication()).getBoolean(key, false);
    }

    public static void delete(String key) {
        PreferenceManager.getDefaultSharedPreferences(ApplicationComponent
                .Instance
                .get()
                .getApplication()).edit().remove(key).apply();
    }

    public static void clearAll() {
        PreferenceManager.getDefaultSharedPreferences(ApplicationComponent
                .Instance
                .get()
                .getApplication()).edit().clear().apply();
    }
}