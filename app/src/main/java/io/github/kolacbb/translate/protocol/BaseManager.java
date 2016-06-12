package io.github.kolacbb.translate.protocol;

import javax.inject.Inject;

import io.github.kolacbb.translate.inject.component.ApplicationComponent;

/**
 * Created by Kola on 2016/6/12.
 */
public abstract class BaseManager {
    @Inject
    ClientApi api;

    public BaseManager() {
        ApplicationComponent.Instance.get().inject(this);
    }

    public ClientApi getApi() {
        return api;
    }
}
