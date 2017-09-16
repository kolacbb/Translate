package io.github.kolacbb.translate;

import android.app.Application;
import android.util.Log;

import io.github.kolacbb.translate.inject.component.DaggerApplicationComponent;
import io.github.kolacbb.translate.inject.component.ApplicationComponent;

import io.github.kolacbb.translate.inject.modules.ApplicationModule;

/**
 * Created by Kola on 2016/6/4.
 */
public class TranslateApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //TranslateDB.init(getApplicationContext());
        Log.e("TransalteApp", "Database init");
        initDagger();
    }

    private void initDagger() {
        ApplicationComponent.Instance.init(DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build());
    }
}
