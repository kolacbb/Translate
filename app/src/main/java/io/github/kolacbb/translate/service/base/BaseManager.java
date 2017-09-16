package io.github.kolacbb.translate.service.base;

import android.app.Application;

import javax.inject.Inject;

import io.github.kolacbb.translate.data.local.TranslateDB;
import io.github.kolacbb.translate.inject.component.ApplicationComponent;
import io.github.kolacbb.translate.protocol.ClientApi;

/**
 * Created by Kola on 2016/6/12.
 */
public abstract class BaseManager {
    @Inject
    ClientApi api;
    @Inject
    TranslateDB translateDB;
    @Inject
    Application application;

    public BaseManager() {
        ApplicationComponent.Instance.get().inject(this);
    }

    public ClientApi getApi() {
        return api;
    }

    public TranslateDB getTranslateDB() {
        return translateDB;
    }

    public Application getApplication() {
        return application;
    }
}