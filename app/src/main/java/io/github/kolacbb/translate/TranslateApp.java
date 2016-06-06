package io.github.kolacbb.translate;

import android.app.Application;
import android.preference.PreferenceManager;

import io.github.kolacbb.translate.util.SpUtil;

/**
 * Created by Kola on 2016/6/4.
 */
public class TranslateApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SpUtil.init(PreferenceManager.getDefaultSharedPreferences(this));
    }

}
