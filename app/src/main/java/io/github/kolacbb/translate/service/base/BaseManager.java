package io.github.kolacbb.translate.service.base;

import javax.inject.Inject;

import io.github.kolacbb.translate.inject.component.ApplicationComponent;
import io.github.kolacbb.translate.protocol.ClientApi;

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