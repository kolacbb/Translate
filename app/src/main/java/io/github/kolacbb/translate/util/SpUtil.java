package io.github.kolacbb.translate.util;

import android.preference.PreferenceManager;

import io.github.kolacbb.translate.inject.component.ApplicationComponent;

/**
 * Created by Kola on 2016/5/16.
 */
public class SpUtil {
    public static void saveOrUpdate(String key, String json) {
        PreferenceManager.getDefaultSharedPreferences(ApplicationComponent
                .Instance
                .get()
                .getApplication())
                .edit().putString(key, json).apply();
    }

    public static String find(String key) {
        return PreferenceManager.getDefaultSharedPreferences(ApplicationComponent
                .Instance
                .get()
                .getApplication()).getString(key, null);
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