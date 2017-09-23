package io.github.kolacbb.translate;

import android.app.Application;
import android.preference.PreferenceManager;
import android.util.Log;

import io.github.kolacbb.translate.data.local.TranslateDB;
import io.github.kolacbb.translate.util.SpUtil;

/**
 * Created by Kola on 2016/6/4.
 */
public class TranslateApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TranslateDB.init(getApplicationContext());
        Log.e("TransalteApp", "Database instance init");
        SpUtil.init(PreferenceManager.getDefaultSharedPreferences(this));
    }
}
